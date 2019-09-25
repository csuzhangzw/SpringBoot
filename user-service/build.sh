#!/usr/bin/env bash

mvn package

docker build -t user-service:latest .

docker run -d -p 8081:8081 --name user-service user-service:latest