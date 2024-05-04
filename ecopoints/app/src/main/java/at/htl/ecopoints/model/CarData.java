package at.htl.ecopoints.model;

import java.sql.Timestamp;
import java.util.UUID;

public class CarData {
    private Long id;
    private UUID TripId;
    private double longitude;
    private double latitude;
    private double currentEngineRPM;
    private double speed;
    private double throttlePosition;
    private String engineRunTime;
    private Timestamp timeStamp;


    public CarData() {
    }

    public CarData(Long id, UUID tripId, double longitude, double latitude, double currentEngineRPM, double speed, double throttlePosition, String engineRunTime,  Timestamp timeStamp) {
        TripId = tripId;
        this.id = id;
        this.currentEngineRPM = currentEngineRPM;
        this.speed = speed;
        this.throttlePosition = throttlePosition;
        this.latitude = latitude;
        this.longitude = longitude;
        this.engineRunTime = engineRunTime;
        this.timeStamp = timeStamp;
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

    public double getCurrentEngineRPM() {
        return currentEngineRPM;
    }

    public void setCurrentEngineRPM(double currentEngineRPM) {
        this.currentEngineRPM = currentEngineRPM;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getThrottlePosition() {
        return throttlePosition;
    }

    public void setThrottlePosition(double throttlePosition) {
        this.throttlePosition = throttlePosition;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEngineRunTime() {
        return engineRunTime;
    }

    public void setEngineRunTime(String engineRunTime) {
        this.engineRunTime = engineRunTime;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}

