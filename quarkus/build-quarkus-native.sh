#!/bin/sh
mvn clean package && docker build -f Dockerfile-quarkus-native -t rest-crud-quarkus-native .

