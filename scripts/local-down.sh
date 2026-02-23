#!/usr/bin/env bash
set -e

docker compose -f docker-compose.local.yml down -v
echo "Local containers stopped."