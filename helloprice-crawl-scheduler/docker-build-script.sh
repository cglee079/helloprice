#!/usr/bin/env bash
cd /volume1/docker-build/helloprice/helloprice-crawl-scheduler
docker stop helloprice-crawl-scheduler
docker rm helloprice-crawl-scheduler
docker rmi helloprice-crawl-scheduler:2.1
docker build --tag helloprice-crawl-scheduler:2.1 .
docker run -it -v /volume1/docker/helloprice/helloprice-crawl-scheduler/logs:/data/logs --name=helloprice-crawl-scheduler helloprice-crawl-scheduler:2.1
