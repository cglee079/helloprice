#!/usr/bin/env bash
cd /volume1/docker-build/podo-helloprice/podo-helloprice-telegram-qa
docker stop podo-helloprice-telegram-qa
docker rm podo-helloprice-telegram-qa
docker rmi podo-helloprice-telegram-qa:1.0
docker build --tag podo-helloprice-telegram-qa:1.0 .
docker run -it -p 18080:8080 -v /volume1/docker/podo-helloprice/podo-helloprice-telegram-qa/logs:/data/logs  --name=podo-helloprice-telegram-qa podo-helloprice-telegram-qa:1.0
