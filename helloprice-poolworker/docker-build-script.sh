#!/usr/bin/env bash
cd /volume1/docker-build/podo-helloprice/podo-helloprice-poolworker
docker stop podo-helloprice-poolworker
docker rm podo-helloprice-poolworker
docker rmi podo-helloprice-poolworker:1.0
docker build --tag podo-helloprice-poolworker:1.0 .
docker run -it -p 17070:8080 -v /volume1/docker/podo-helloprice/podo-helloprice-poolworker/logs:/data/logs --name=podo-helloprice-poolworker podo-helloprice-poolworker:1.0
