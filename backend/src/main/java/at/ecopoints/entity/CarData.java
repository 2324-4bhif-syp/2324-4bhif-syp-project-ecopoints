package at.ecopoints.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class CarData {
    //id, TripId(UUID, OID), longitude, latitude,  currentEngineRPM, currentVelocity, ThrottlePosition, EngineRunTime, TimeStamp

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tripId;
    private double longitude;
    private double latitude;
    private double currentEngineRPM;
    private double currentVelocity;
    private double throttlePosition;
    private String engineRunTime;
    private Timestamp timeStamp;


    //region Constructors
    public CarData(){}
    public CarData(Long tripId, double longitude, double latitude, double currentEngineRPM, double currentVelocity, double throttlePosition, String engineRunTime, Timestamp timeStamp) {
        this.tripId = tripId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.currentEngineRPM = currentEngineRPM;
        this.currentVelocity = currentVelocity;
        this.throttlePosition = throttlePosition;
        this.engineRunTime = engineRunTime;
        this.timeStamp = timeStamp;
    }
    //endregion

    //region Getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getCurrentEngineRPM() {
        return currentEngineRPM;
    }

    public void setCurrentEngineRPM(double currentEngineRPM) {
        this.currentEngineRPM = currentEngineRPM;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }

    public void setCurrentVelocity(double currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public double getThrottlePosition() {
        return throttlePosition;
    }

    public void setThrottlePosition(double throttlePosition) {
        this.throttlePosition = throttlePosition;
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
    //endregion
}
