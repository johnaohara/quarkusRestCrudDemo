#!/bin/sh
mvn clean package && docker build -f Dockerfile-quarkus-vertx-native -t rest-crud-quarkus-vertx-native .

