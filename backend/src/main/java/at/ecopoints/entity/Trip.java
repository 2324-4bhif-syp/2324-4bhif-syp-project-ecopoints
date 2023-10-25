package at.ecopoints.entity;

import java.util.Date;

public class Trip {
    private Long id;
    private double distance;
    private double avgSpeed;
    private double avgEngineRotation;
    private Date date;
    private double rewardedEcoPoints;

    // region Constructors
    public Trip() {}

    public Trip(double distance, double avgSpeed, double avgEngineRotation, Date date, double rewardedEcoPoints) {
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.avgEngineRotation = avgEngineRotation;
        this.date = date;
        this.rewardedEcoPoints = rewardedEcoPoints;
    }
    // endregion
    
    // region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgEngineRotation() {
        return avgEngineRotation;
    }

    public void setAvgEngineRotation(double avgEngineRotation) {
        this.avgEngineRotation = avgEngineRotation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRewardedEcoPoints() {
        return rewardedEcoPoints;
    }

    public void setRewardedEcoPoints(double rewardedEcoPoints) {
        this.rewardedEcoPoints = rewardedEcoPoints;
    }
    // endregion
}
