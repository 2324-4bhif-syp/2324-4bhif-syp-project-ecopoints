package at.ecopoints.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "ECO_CARDATA")
@NamedQueries(
        {
                @NamedQuery(
                        name = "CarData.findAll",
                        query = "select c from CarData c"
                ),
                @NamedQuery(
                        name = "CarData.findByTripId",
                        query = "select c from CarData c where c.trip = :tripId"
                )
        }
)
public class CarData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE
    })
    private Trip trip;

    //region Constructors
    public CarData(){}
    public CarData(double longitude, double latitude, double currentEngineRPM, double currentVelocity, double throttlePosition, String engineRunTime, Timestamp timeStamp, Trip trip) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.currentEngineRPM = currentEngineRPM;
        this.currentVelocity = currentVelocity;
        this.throttlePosition = throttlePosition;
        this.engineRunTime = engineRunTime;
        this.timeStamp = timeStamp;
        this.trip = trip;
    }
    //endregion

    //region Getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    //endregion


    @Override
    public String toString() {
        return String.format("CarData{longitude=%s, latitude=%s," +
                " currentEngineRPM=%s, currentVelocity=%s, throttlePosition=%s, engineRunTime=%s, " +
                        "timeStamp=%s, trip=%s}",
                longitude, latitude, currentEngineRPM, currentVelocity,
                throttlePosition, engineRunTime, timeStamp, trip.getId());
    }
}
