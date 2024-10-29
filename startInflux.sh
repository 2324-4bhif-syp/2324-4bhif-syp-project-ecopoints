#!/bin/bash

# Check if InfluxDB container is already running
if [ "$(docker ps -q -f name=influxdb)" ]; then
    echo "InfluxDB container is already running."
else
    # Check if the container exists (but is not running)
    if [ "$(docker ps -aq -f status=exited -f name=influxdb)" ]; then
        echo "Starting existing InfluxDB container..."
        docker start influxdb
    else
        # If the container does not exist, create and run it
        echo "Creating and starting a new InfluxDB container..."
        docker run -d -p 8086:8086 \
            -v "$(pwd)/influxdb/data:/var/lib/influxdb" \
            -v "$(pwd)/influxdb/config:/etc/influxdb" \
            --name influxdb \
            influxdb:latest
    fi
fi

# Pause the script to keep the terminal open (only needed if run in a non-interactive shell)
read -p "Press [Enter] key to continue..."
