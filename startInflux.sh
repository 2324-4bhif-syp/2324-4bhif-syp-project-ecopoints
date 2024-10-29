#!/bin/bash

CONTAINER_NAME="influxdb"
IMAGE_NAME="influxdb:latest"
PORT="8086"

# Check if the container is already running
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Container '$CONTAINER_NAME' is already running. Starting it..."
    docker start $CONTAINER_NAME
else
    echo "Container '$CONTAINER_NAME' does not exist. Pulling the latest image and creating a new container..."
    docker pull $IMAGE_NAME
    docker run -d --name $CONTAINER_NAME -p $PORT:$PORT $IMAGE_NAME
fi

echo "InfluxDB container is up and running."
