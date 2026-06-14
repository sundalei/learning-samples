# datafaker-demo

Maven sample that uses DataFaker to generate fake customer data and stores it in MySQL.

## What It Does

- Starts from `sample.Main`.
- Reads database settings from `src/main/resources/mysql-properties.xml`.
- Generates 10 fake customers with `net.datafaker`.
- Inserts the rows into the `customer` table.
- Prints the contents of the table after insertion.

The generated names use `Locale.CHINESE` in `sample.DataUtil`.

## Requirements

- Java 17
- Maven
- Docker and Docker Compose

## Run

Start MySQL from this project directory:

```bash
cd datafaker-demo
docker compose up -d
```

Run the sample from the repository root:

```bash
mvn -pl datafaker-demo -am compile exec:java -Dexec.mainClass=sample.Main
```

Run tests for this module:

```bash
mvn -pl datafaker-demo -am test
```

## Database

The Docker Compose service starts a MySQL container with:

- Database: `dalei`
- User: `root`
- Password: `Fnst*1234`
- Port: `3306`

`init.sql` creates the `customer` table if it does not already exist.

## Notes

- Each run inserts 10 more rows.
- The database data is stored in the Docker volume `db_data`.
- If port `3306` is already in use, change the port mapping in `compose.yaml`.
- The password in this sample is for local learning only. Do not reuse it in real projects.
