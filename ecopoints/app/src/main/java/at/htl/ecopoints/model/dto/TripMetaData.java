package at.htl.ecopoints.model.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import at.htl.ecopoints.model.LatLng;

public class TripMetaData {
    private String startDate;
    private String endDate;
    private String duration;
    private UUID tripId;
    private double distance;
    private double averageSpeedGps;
    private double averageSpeedObd;
    private double maxSpeedGps;
    private double maxSpeedObd;
    private double averageRpm;
    private double averageEngineLoad;
    private EcoPointsMetaData ecoPointsMetaData;
    private List<LatLng> latLngList;

    public TripMetaData() {
    }

    // Getters and Setters


    public double getAverageEngineLoad() {
        return averageEngineLoad;
    }

    public void setAverageEngineLoad(double averageEngineLoad) {
        this.averageEngineLoad = averageEngineLoad;
    }

    public EcoPointsMetaData getEcoPointsMetaData() {
        return ecoPointsMetaData;
    }

    public void setEcoPointsMetaData(EcoPointsMetaData ecoPointsMetaData) {
        this.ecoPointsMetaData = ecoPointsMetaData;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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

    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    public void setLatLngList(List<LatLng> latLngList) {
        this.latLngList = latLngList;
    }
}
