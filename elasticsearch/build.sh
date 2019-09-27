#!/usr/bin/env bash

mvn package

docker build -t elasticsearch:latest .

docker run -d -p 8080:8080 --link zookeeper:zookeeper_host --name elasticsearch elasticsearch:latest