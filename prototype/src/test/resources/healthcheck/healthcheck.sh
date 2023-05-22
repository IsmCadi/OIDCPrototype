#!/bin/bash

set -eo pipefail

# Replace `http://localhost:8080` with the appropriate Keycloak URL
KEYCLOAK_URL="http://localhost:8080"

# Make a GET request to Keycloak's server info endpoint
response=$(wget --timeout=5 --tries=1 -qO- $KEYCLOAK_URL/auth/realms/master)

# Check if the response contains a specific string that indicates Keycloak is healthy
if [[ $response == *"version"* ]]; then
    exit 0 # Keycloak is healthy
else
    exit 1 # Keycloak is not healthy
fi
