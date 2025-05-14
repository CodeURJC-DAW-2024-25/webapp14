#!/bin/bash

# Define variables
IMAGE_NAME="docker.io/iciar04/spring-webapp-compose"
TAG="0.1.0"
COMPOSE_FILE="docker-compose.prod.yml"

# Log in to Docker Hub
docker login --username iciar04 --password 12345docker

# Publish the file as OCI Artifact
oras push $IMAGE_NAME:$TAG --artifact-type application/vnd.docker.compose.v1+yaml $COMPOSE_FILE

echo "docker-compose file has been pushed as an OCI Artifact"