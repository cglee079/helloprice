#!/usr/bin/env bash
cd /volume1/docker-build/helloprice/helloprice-crawl-agent
docker stop helloprice-crawl-agent
docker rm helloprice-crawl-agent
docker rmi helloprice-crawl-agent:2.1
docker build --tag helloprice-crawl-agent:2.1 .
docker run -it -v /volume1/docker/helloprice/helloprice-crawl-agent/logs:/data/logs --name=helloprice-crawl-agent helloprice-crawl-agent:2.1
