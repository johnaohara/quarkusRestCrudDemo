#!/bin/sh
mvn clean package -Dno-native && docker build -f Dockerfile-quarkus-jvm -t rest-crud-quarkus-jvm .
