#!/bin/sh
mvn clean package && docker build -f Dockerfile-thorntail -t rest-crud-thorntail .
