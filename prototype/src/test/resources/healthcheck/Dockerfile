FROM alpine:latest

# Install necessary dependencies
RUN apk --no-cache add curl

# Copy your healthcheck script to the container
COPY healthcheck.sh /usr/local/bin/healthcheck.sh

# Set the healthcheck script as the entrypoint
ENTRYPOINT ["healthcheck.sh"]