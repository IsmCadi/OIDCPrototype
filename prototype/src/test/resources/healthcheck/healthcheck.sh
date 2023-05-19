#!/bin/sh

KEYCLOAK_URL="http://localhost:8080/health/ready"

# Delay before starting the healthcheck
DELAY=5
echo "Delaying healthcheck for $DELAY seconds..."
sleep $DELAY

# Retry logic for checking Keycloak's health
MAX_RETRIES=30
RETRY_DELAY=5

# Performs the healthcheck
for i in $(seq 1 $MAX_RETRIES); do
  response=$(curl -s -o /dev/null -w "%{http_code}" "$KEYCLOAK_URL")

  if [ "$response" = "200" ]; then
    echo "Keycloak is ready."
    exit 0
  fi

  echo "Keycloak is not ready yet. Retrying in $RETRY_DELAY seconds..."
  sleep $RETRY_DELAY
done

echo "Keycloak healthcheck failed after $MAX_RETRIES retries."
exit 1