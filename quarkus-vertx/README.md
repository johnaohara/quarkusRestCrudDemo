# Requirements

- PostgreSQL database
- JDK 1.8
- Graal VM 1.0
- Apache Maven
- Quarkus

If you don't have Graal VM installed, you can download it here:

<https://github.com/oracle/graal/releases>

# Bulding the demo

Ensure that `GRAALVM_HOME` points to your `GraalVM/Contents/Home` directory:

> export GRAALVM_HOME=/my/path/to/GraalVM/Contents/Home

Ensure that you are in the quarkus-vertx directory:

> cd ./quarkus-vertx/

Build with Maven:

> mvn package

# Running the demo

First launch the PosgreSQL database:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name postgres-quarkus-rest-http-crud -e POSTGRES_USER=restcrud -e POSTGRES_PASSWORD=restcrud -e POSTGRES_DB=rest-crud -p 5432:5432 postgres:10.5

To run the application in interactive mode (developer mode):

>  mvn quarkus:dev

If you see an error message similar to `[ERROR] Failed to execute goal io.quarkus:quarkus-maven-plugin:0.20.0:dev (default-cli) on project rest-http-crud-quarkus: The project Quarkus - Relational Database Backend Mission has no output yet. Make sure it contains at least one source or resource file and then run `mvn compile quarkus:dev`. -> [Help 1]`, please confirm that you have previsouly compiled the project with;

> mvn compile quarkus:dev

Alternatively,  you can start the application normally using the -runner.jar;

> java -jar ./target/rest-http-crud-quarkus-vertx-runner.jar

To run the native image:

> ./target/rest-http-crud-quarkus-vertx-1.0.0.Alpha1-SNAPSHOT-runner

# Testing the demo

Navigate to:

<http://localhost:8080/index.html>


