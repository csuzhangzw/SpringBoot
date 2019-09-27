#!/usr/bin/env bash

mvn package

docker build -t user-service:latest .

docker run -d -p 8081:8081 -p 20880:20880 --link mysql:mysql_host --name userservice user-service:latest