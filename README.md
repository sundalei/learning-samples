# learning-samples

> Small collection of Java learning samples: a DataFaker + MySQL demo and a Gson examples project.

## Projects

- `datafaker-demo` — Maven module demonstrating `net.datafaker` to generate fake `Customer` rows and persist them to MySQL. Includes a `docker-compose` to run a local MySQL instance and initialization SQL.
- `gson-learning` — Gradle sample showing `gson` serialization/deserialization examples and a `GsonDemo` entrypoint.

## Requirements

- Java 17 (project `maven-compiler-plugin` is configured with `release=17`).
- Maven (for the root project and `datafaker-demo`).
- Gradle (or the Gradle wrapper) for `gson-learning`.
- Docker & Docker Compose (optional) to run the MySQL database used by `datafaker-demo`.

## datafaker-demo

Location: `datafaker-demo`

What it does:
- Loads DB connection properties from `src/main/resources/mysql-properties.xml`.
- Connects to a MySQL instance and inserts 10 generated `Customer` rows, then prints the table contents.

Run with Docker Compose (recommended):

1. Start MySQL:

```bash
cd datafaker-demo
docker compose up -d
```

2. Build and run the demo with Maven (from repository root):

```bash
mvn -pl datafaker-demo -am compile exec:java -Dexec.mainClass=sample.Main
```

Notes:
- The compose file exposes MySQL on port `3306` and initializes the `dalei` database using `init.sql`.
- Default credentials are set in `datafaker-demo/src/main/resources/mysql-properties.xml` (user: `root`, password: `Fnst*1234`). Remove or change these for production use.

Alternatively you can run the built JAR after packaging:

```bash
mvn -pl datafaker-demo package
java -cp datafaker-demo/target/classes:$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.build.outputDirectory}' --non-recursive exec:exec) sample.Main
```

## gson-learning

Location: `gson-learning`

Run with Gradle:

```bash
cd gson-learning
./gradlew run
```

Or build and run with Gradle installed:

```bash
gradle run
```

The demo reads `src/main/resources/book.json` in `GsonDemo` and prints serialization/deserialization examples.

## Security / Credentials

- `datafaker-demo/src/main/resources/mysql-properties.xml` currently contains a plaintext password. Do not commit real credentials; consider using environment variables or a secret manager for real projects.

## Tests

- The root POM configures JUnit Jupiter. Run tests with:

```bash
mvn test
```

or for the Gradle sample:

```bash
cd gson-learning
./gradlew test
```

## Notes & Tips

- If `docker compose up` fails due to port conflicts, stop any process using port `3306` or change the mapping in `datafaker-demo/compose.yaml`.
- To inspect the MySQL container logs:

```bash
docker compose -f datafaker-demo/compose.yaml logs -f
```

## License

This repository contains small examples for learning purposes. No license file is included.
