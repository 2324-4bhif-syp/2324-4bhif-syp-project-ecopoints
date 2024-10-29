@echo off
SET CONTAINER_NAME=influxdb
SET IMAGE_NAME=influxdb:latest
SET PORT=8086

REM Check if the container is already running
docker ps -q -f name=%CONTAINER_NAME% | findstr . >nul
IF %ERRORLEVEL% EQU 0 (
    echo Container "%CONTAINER_NAME%" is already running. Starting it...
    docker start %CONTAINER_NAME%
) ELSE (
    echo Container "%CONTAINER_NAME%" does not exist. Pulling the latest image and creating a new container...
    docker pull %IMAGE_NAME%
    docker run -d --name %CONTAINER_NAME% -p %PORT%:%PORT% %IMAGE_NAME%
)

echo InfluxDB container is up and running.
