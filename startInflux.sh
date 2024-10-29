#!/bin/bash

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker."
    exit 1
fi

# Pull the latest InfluxDB image
echo "Pulling the latest InfluxDB image..."
docker pull influxdb:latest

# Stop and remove existing container if it exists
echo "Stopping and removing existing InfluxDB container if it exists..."
docker stop influxdb > /dev/null 2>&1
docker rm influxdb > /dev/null 2>&1

# Start a new InfluxDB container
echo "Starting InfluxDB container..."
docker run --name influxdb -p 8086:8086 influxdb:latest

echo "InfluxDB is now running on http://localhost:8086"
