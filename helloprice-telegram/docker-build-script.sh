#!/usr/bin/env bash
cd /volume1/docker-build/podo-helloprice/podo-helloprice-telegram
docker stop podo-helloprice-telegram
docker rm podo-helloprice-telegram
docker rmi podo-helloprice-telegram:2.0
docker build --tag podo-helloprice-telegram:2.0 .
docker run -it -v /volume1/docker/podo-helloprice/podo-helloprice-telegram/logs:/data/logs  --name=podo-helloprice-telegram podo-helloprice-telegram:2.0
