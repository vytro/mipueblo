#!/bin/bash
export ENV_FILE=./.env.dev
set -a
source ./.env.dev
set +a
docker-compose up -d --build