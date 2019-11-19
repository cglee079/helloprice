#!/usr/bin/env bash
cd /volume1/docker-build/podo-helloprice/podo-helloprice-crawlworker
docker stop podo-helloprice-crawlworker
docker rm podo-helloprice-crawlworker
docker rmi podo-helloprice-crawlworker:2.0
docker build --tag podo-helloprice-crawlworker:2.0 .
docker run -it -v /volume1/docker/podo-helloprice/podo-helloprice-crawlworker/logs:/data/logs --name=podo-helloprice-crawlworker podo-helloprice-crawlworker:2.0
