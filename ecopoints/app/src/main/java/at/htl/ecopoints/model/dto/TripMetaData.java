package at.htl.ecopoints.model.dto;

import android.util.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TripMetaData {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Duration duration;
    private UUID tripId;
    private double distance;
    private double averageSpeedGps;
    private double averageSpeedObd;
    private double maxSpeedGps;
    private double maxSpeedObd;
    private double averageRpm;
    private List<Pair<Double, Double>> latLngList;

    // Getters and Setters
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAverageSpeedGps() {
        return averageSpeedGps;
    }

    public void setAverageSpeedGps(double averageSpeedGps) {
        this.averageSpeedGps = averageSpeedGps;
    }

    public double getAverageSpeedObd() {
        return averageSpeedObd;
    }

    public void setAverageSpeedObd(double averageSpeedObd) {
        this.averageSpeedObd = averageSpeedObd;
    }

    public double getMaxSpeedGps() {
        return maxSpeedGps;
    }

    public void setMaxSpeedGps(double maxSpeedGps) {
        this.maxSpeedGps = maxSpeedGps;
    }

    public double getMaxSpeedObd() {
        return maxSpeedObd;
    }

    public void setMaxSpeedObd(double maxSpeedObd) {
        this.maxSpeedObd = maxSpeedObd;
    }

    public double getAverageRpm() {
        return averageRpm;
    }

    public void setAverageRpm(double averageRpm) {
        this.averageRpm = averageRpm;
    }

    public List<Pair<Double, Double>> getLatLngList() {
        return latLngList;
    }

    public void setLatLngList(List<Pair<Double, Double>> latLngList) {
        this.latLngList = latLngList;
    }
}
