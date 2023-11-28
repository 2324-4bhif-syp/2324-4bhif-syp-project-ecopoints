CREATE TABLE IF NOT EXISTS car_data (
id INTEGER Primary Key Autoincrement,
longitude REAL,
latitude REAL,
current_engine_rpm REAL,
current_velocity REAL,
throttle_position REAL,
engine_run_time TEXT,
timestamp TEXT
)

INSERT INTO car_data (longitude, latitude, current_engine_rpm, current_velocity, throttle_position, engine_run_time, timestamp)
VALUES
    (10.0, 20.0, 3000.0, 60.0, 0.7, '10 hours', '2023-01-01 12:00:00'),
    (15.0, 25.0, 3500.0, 65.0, 0.8, '12 hours', '2023-01-01 14:00:00');