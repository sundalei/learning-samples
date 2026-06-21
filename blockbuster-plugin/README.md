# Blockbuster Elasticsearch Plugin

This module demonstrates how to write, package, install, and test a custom
Elasticsearch script plugin. The plugin adds a script language named `java`
with one supported update script, `CustomBlockBusterCheck`.

The script compares a movie's `boxoffice_gross_in_millions` field with the
`gross_earnings_threshold` script parameter and writes the result to a
`blockbuster` field.

## Requirements

- Java 17
- Maven
- Elasticsearch 9.4.2
- Docker, if using the Docker deployment example

Elasticsearch plugins must be built for the exact Elasticsearch version on
which they are installed. The Maven property in the parent `pom.xml`, the
`elasticsearch.version` in `plugin-descriptor.properties`, and the running
Elasticsearch version must match.

## How the Plugin Is Written

The plugin consists of three Java classes:

- `BlockBusterPlugin` is the Elasticsearch entry point. It implements
  `ScriptPlugin` and creates the custom script engine.
- `BlockBusterScriptEngine` registers the `java` language, accepts the
  `CustomBlockBusterCheck` source, and supports the Elasticsearch update-script
  context.
- `CustomBlockBusterCheck` contains the business logic and is kept independent
  of Elasticsearch so it can be unit tested directly.

Elasticsearch discovers the entry point through
`src/main/resources/plugin-descriptor.properties`:

```properties
name=blockbuster-script-plugin
classname=com.example.BlockBusterPlugin
java.version=17
elasticsearch.version=9.4.2
```

To add another script:

1. Create a class containing the script's business logic.
2. Add a source-name branch in `BlockBusterScriptEngine.compile`.
3. Return a factory for each Elasticsearch script context the script supports.
4. Include those contexts in `getSupportedContexts`.
5. Add unit tests for the business logic and an Elasticsearch integration test.

Do not accept arbitrary Java source in `compile`. A plugin script engine should
map known source names to reviewed implementations.

## Build and Unit Test

From this module:

```bash
mvn test
mvn package
```

Or from the repository root:

```bash
mvn -pl blockbuster-plugin -am test
mvn -pl blockbuster-plugin -am package
```

The package command creates:

```text
target/blockbuster-plugin-0.0.1-SNAPSHOT-plugin.zip
```

The ZIP contains the plugin JAR and `plugin-descriptor.properties`. Check it
with:

```bash
unzip -l target/blockbuster-plugin-0.0.1-SNAPSHOT-plugin.zip
```

## Deploy to Elasticsearch

### Existing Elasticsearch Installation

Stop Elasticsearch, install the ZIP with the plugin manager from the
Elasticsearch installation directory, and start Elasticsearch again:

```bash
bin/elasticsearch-plugin install \
  file:///absolute/path/to/blockbuster-plugin/target/blockbuster-plugin-0.0.1-SNAPSHOT-plugin.zip
```

Use an absolute `file://` URL. Every node that may execute the script must have
the plugin installed, and each node must be restarted after installation.

Confirm installation with:

```bash
bin/elasticsearch-plugin list
```

Expected output includes:

```text
blockbuster-script-plugin
```

To remove the plugin, stop Elasticsearch and run:

```bash
bin/elasticsearch-plugin remove blockbuster-script-plugin
```

### Local Docker Deployment

First build the plugin:

```bash
mvn package
```

Then start a single-node Elasticsearch container, installing the plugin before
Elasticsearch starts:

```bash
docker run --rm --name blockbuster-es \
  -p 9200:9200 \
  -e discovery.type=single-node \
  -e xpack.security.enabled=false \
  -v "$PWD/target/blockbuster-plugin-0.0.1-SNAPSHOT-plugin.zip:/tmp/plugin.zip:ro" \
  docker.elastic.co/elasticsearch/elasticsearch:9.4.2 \
  bash -c 'bin/elasticsearch-plugin install --batch file:///tmp/plugin.zip &&
    exec /usr/local/bin/docker-entrypoint.sh elasticsearch'
```

This disables security only to simplify local testing. Do not use that setting
for a production deployment.

Wait until Elasticsearch is ready:

```bash
curl --fail http://localhost:9200
```

## Integration Test

Create a movie document:

```bash
curl --request PUT \
  --header 'Content-Type: application/json' \
  http://localhost:9200/movies/_doc/1 \
  --data '{
    "title": "Example Movie",
    "boxoffice_gross_in_millions": 134.8
  }'
```

Run the custom update script:

```bash
curl --request POST \
  --header 'Content-Type: application/json' \
  http://localhost:9200/movies/_update/1 \
  --data '{
    "script": {
      "lang": "java",
      "source": "CustomBlockBusterCheck",
      "params": {
        "gross_earnings_threshold": 125
      }
    }
  }'
```

Read the updated document:

```bash
curl http://localhost:9200/movies/_doc/1
```

The response source should contain:

```json
{
  "title": "Example Movie",
  "boxoffice_gross_in_millions": 134.8,
  "blockbuster": true
}
```

The comparison is strictly greater than. A gross value equal to the threshold
produces `"blockbuster": false`.

Stop the Docker test instance with:

```bash
docker stop blockbuster-es
```

## Troubleshooting

- **Plugin was built for a different Elasticsearch version:** align all
  Elasticsearch versions, rebuild the ZIP, reinstall it, and restart the node.
- **Unknown script language `java`:** verify the plugin is listed on the node
  handling the request and that the node was restarted after installation.
- **Unsupported script or context:** this implementation accepts only
  `source: "CustomBlockBusterCheck"` in the update-script context.
- **Missing field or parameter:** both `boxoffice_gross_in_millions` and
  `gross_earnings_threshold` must exist and contain numeric values.
