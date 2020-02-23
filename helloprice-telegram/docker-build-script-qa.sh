#!/usr/bin/env bash
cd /volume1/docker-build/helloprice/helloprice-telegram-qa
docker stop helloprice-telegram-qa
docker rm helloprice-telegram-qa
docker rmi helloprice-telegram-qa:1.0
docker build --tag helloprice-telegram-qa:1.0 .
docker run -it -p 18080:8080 -v /volume1/docker/helloprice/helloprice-telegram-qa/logs:/data/logs  --name=helloprice-telegram-qa helloprice-telegram-qa:1.0
