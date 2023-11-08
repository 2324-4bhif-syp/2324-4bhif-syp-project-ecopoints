package at.ecopoints.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class CarData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("trip_id")
    private UUID tripId;
    private double longitude;
    private double latitude;

    @JsonProperty("current_engine_rpm")
    private double currentEngineRPM;
    @JsonProperty("current_velocity")
    private double currentVelocity;
    @JsonProperty("throttle_position")
    private double throttlePosition;
    @JsonProperty("engine_run_time")
    private String engineRunTime;
    @JsonProperty("time_stamp")
    private Timestamp timeStamp;

    //region Constructors
    public CarData(){}
    public CarData(UUID tripId, double longitude, double latitude, double currentEngineRPM, double currentVelocity, double throttlePosition, String engineRunTime, Timestamp timeStamp) {
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

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
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
