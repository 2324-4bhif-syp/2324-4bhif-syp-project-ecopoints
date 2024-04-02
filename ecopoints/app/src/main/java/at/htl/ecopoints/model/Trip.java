package at.htl.ecopoints.model;

import java.util.Date;
import java.util.UUID;

public class Trip {
    private UUID id;
    private Date start;
    private Date end;
    private double distance;
    private double avgFuelConsumption;
    private double avgSpeed;
    private double avgRpm;

    public Trip() {
    }

    public Trip(UUID id, Date start, Date end, double distance, double avgFuelConsumption, double avgSpeed, double avgRpm) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.avgFuelConsumption = avgFuelConsumption;
        this.avgSpeed = avgSpeed;
        this.avgRpm = avgRpm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAvgFuelConsumption() {
        return avgFuelConsumption;
    }

    public void setAvgFuelConsumption(double avgFuelConsumption) {
        this.avgFuelConsumption = avgFuelConsumption;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgRpm() {
        return avgRpm;
    }

    public void setAvgRpm(double avgRpm) {
        this.avgRpm = avgRpm;
    }
}
