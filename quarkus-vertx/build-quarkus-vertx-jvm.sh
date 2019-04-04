#!/bin/sh
mvn clean package -Dno-native && docker build -f Dockerfile-quarkus-vertx-jvm -t rest-crud-quarkus-vertx-jvm .
