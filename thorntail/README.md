# Requirements

- PostgreSQL database
- JDK 1.8
- Apache Maven

# Bulding the demo

Ensure that you are in the thorntail directory:

> cd ./thorntail/

Build with Maven:

> mvn package

# Running the demo

First launch the PosgreSQL database:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name postgres-quarkus-rest-http-crud -e POSTGRES_USER=restcrud -e POSTGRES_PASSWORD=restcrud -e POSTGRES_DB=rest-crud -p 5432:5432 postgres:10.5

To run the application in interactive mode (developer mode):

>  run-thorntail.sh

# Testing the demo

Navigate to:

<http://localhost:8080/index.html>


