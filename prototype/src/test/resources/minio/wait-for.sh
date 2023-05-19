#!/bin/bash

KEYCLOAK_URL="http://localhost:8080/health/ready" # Update with the actual Keycloak URL
MINIO_CONTAINER="minio" # Update with the name of your MinIO container

# Wait for Keycloak to become ready
echo "Waiting for Keycloak to become ready..."
until $(curl --output /dev/null --silent --head --fail "$KEYCLOAK_URL"); do
    printf '.'
    sleep 1
done

# Start MinIO container
echo -e "\nKeycloak is ready. Starting MinIO container..."
docker start "$MINIO_CONTAINER"