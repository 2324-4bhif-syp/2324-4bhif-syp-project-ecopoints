@echo off

rem Command to run InfluxDB Docker container
docker run -d -p 8086:8086 ^
    -v %CD%\influxdb\data:/var/lib/influxdb ^
    -v %CD%\influxdb\config:/etc/influxdb ^
    --name influxdb ^
    influxdb:latest

pause
