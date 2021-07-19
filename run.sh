#!/usr/bin/env bash
echo "***********************************"
echo "Building sync-app..."
echo "***********************************"

./gradlew clean build

echo "***********************************"
echo "Starting postgres and sync-app..."
echo "***********************************"

docker-compose up
