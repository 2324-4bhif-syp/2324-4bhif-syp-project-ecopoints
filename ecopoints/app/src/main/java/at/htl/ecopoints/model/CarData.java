package at.htl.ecopoints.model;

import java.sql.Timestamp;
import java.util.UUID;

public class CarData {
    private UUID TripId;
    private Long id;
    private double rpm;
    private double speed;
    private double throttlePos;
    private String lat;
    private String lng;
    private String fuelConsumption;
    private Timestamp timestamp;

    public CarData() {
    }

    public CarData(UUID tripId, Long id, double rpm, double speed, double throttlePos, String lat, String lng, String fuelConsumption, Timestamp timestamp) {
        TripId = tripId;
        this.id = id;
        this.rpm = rpm;
        this.speed = speed;
        this.throttlePos = throttlePos;
        this.lat = lat;
        this.lng = lng;
        this.fuelConsumption = fuelConsumption;
        this.timestamp = timestamp;
    }

    public UUID getTripId() {
        return TripId;
    }

    public void setTripId(UUID tripId) {
        TripId = tripId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getThrottlePos() {
        return throttlePos;
    }

    public void setThrottlePos(double throttlePos) {
        this.throttlePos = throttlePos;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(String fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

