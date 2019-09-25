#!/usr/bin/env bash

mvn package

docker build -t elasticsearch:latest .

docker run -d -p 8080:8080 elasticsearch:latest