#!/usr/bin/env bash
set -e

docker compose -f docker-compose.local.yml up --build -d

echo ""
echo "Local containers started:"
echo "UI:  http://localhost:8081"
echo "API: http://localhost:8080/api"