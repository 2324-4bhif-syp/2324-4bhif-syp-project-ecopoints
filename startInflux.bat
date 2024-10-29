@echo off
REM Check if Docker is running
docker info >nul 2>&1
IF ERRORLEVEL 1 (
    echo Docker is not running. Please start Docker Desktop.
    exit /b
)

REM Pull the latest InfluxDB image
echo Pulling the latest InfluxDB image...
docker pull influxdb:latest

REM Stop and remove existing container if it exists
echo Stopping and removing existing InfluxDB container if it exists...
docker stop influxdb >nul 2>&1
docker rm influxdb >nul 2>&1

REM Start a new InfluxDB container
echo Starting InfluxDB container...
docker run --name influxdb -p 8086:8086 influxdb:latest

echo InfluxDB is now running on http://localhost:8086
