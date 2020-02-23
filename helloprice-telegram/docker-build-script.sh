#!/usr/bin/env bash
cd /volume1/docker-build/helloprice/helloprice-telegram
docker stop helloprice-telegram
docker rm helloprice-telegram
docker rmi helloprice-telegram:2.0
docker build --tag helloprice-telegram:2.0 .
docker run -it -p 18080:8080 -p 25:25 -p 465:465 -p 587:587 -v /volume1/docker/helloprice/helloprice-telegram/logs:/data/logs  --name=helloprice-telegram helloprice-telegram:2.0
