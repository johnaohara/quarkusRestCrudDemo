# Requirements

- PostgreSQL database
- JDK 1.8
- Graal VM 1.0
- Apache Maven
- Quarkus

If you don't have Graal VM installed, you can download it here:

<https://github.com/oracle/graal/releases>

Clone and build Quarkus from GitHub:

<https://github.com/jbossas/quarkus>

# Bulding the demo

Ensure that `GRAALVM_HOME` points to your `GraalVM/Contents/Home` directory:

> export GRAALVM_HOME=/my/path/to/GraalVM/Contents/Home

Build with Maven:

> mvn package

# Running the demo

First launch the PosgreSQL database:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name postgres-quarkus-rest-http-crud -e POSTGRES_USER=restcrud -e POSTGRES_PASSWORD=restcrud -e POSTGRES_DB=rest-crud -p 5432:5432 postgres:10.5

To run the application in interactive mode (developer mode):

>  mvn quarkus:dev

To run the native image:

> ./target/rest-http-crud-quarkus-1.0.0.Alpha1-SNAPSHOT-runner

# Testing the demo

Navigate to:

<http://localhost:8080/index.html>


