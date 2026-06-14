# learning-samples

Small Java learning projects and experiments.

## Projects

| Project | Topic | Build | Notes |
| --- | --- | --- | --- |
| `datafaker-demo` | DataFaker, MySQL, Docker Compose | Maven | Generates fake customer rows and inserts them into a local MySQL table. |
| `httpclient-demo` | HTTP client examples | Maven | Placeholder module for HTTP client learning samples. |

## Requirements

- Java 17
- Maven
- Docker and Docker Compose for samples that need local services

## Common Commands

Run all module tests:

```bash
mvn test
```

Run one module and any modules it depends on:

```bash
mvn -pl datafaker-demo -am test
```

Format all modules with Spotless:

```bash
mvn spotless:apply
```

## Maven Option Notes

`mvn -pl datafaker-demo -am test` means:

- `-pl datafaker-demo`: build only the selected project from the multi-module Maven reactor. `-pl` is short for `--projects`.
- `-am`: also build any required upstream modules that the selected project depends on. `-am` is short for `--also-make`.
- `test`: run Maven's test phase.

In this repository, `datafaker-demo` is listed in the root `pom.xml`, so this command runs tests for that module without running every sample project.

## Documentation Pattern

Keep this root README as a catalog. Add or update one row in `Projects` whenever a sample is added, renamed, or removed.

Put detailed setup notes inside each sample folder when the project has its own runtime, service, credentials, example output, or troubleshooting steps.

## Security

These are learning samples. Do not commit real credentials. Demo-only credentials may appear in local configuration files when they are needed to keep a sample runnable.

## License

No license file is included.
