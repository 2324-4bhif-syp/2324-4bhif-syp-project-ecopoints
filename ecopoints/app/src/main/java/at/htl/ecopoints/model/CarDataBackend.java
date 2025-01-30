package at.htl.ecopoints.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class CarDataBackend {

    @JsonProperty("Latitude")
    private double latitude;

    @JsonProperty("Longitude")
    private double longitude;

    @JsonProperty("Altitude")
    private double altitude;

    @JsonProperty("Engine Load")
    private double engineLoad;

    @JsonProperty("Engine Coolant Temperature")
    private double coolantTemperature;

    @JsonProperty("Engine RPM")
    private double engineRpm;

    @JsonProperty("Gps-Speed")
    private double gpsSpeed;

    @JsonProperty("Vehicle Speed")
    private double obdSpeed;

    public CarDataBackend() {
    }

    public CarDataBackend(double latitude, double longitude, double altitude, double engineLoad,
                   double coolantTemperature, double engineRpm, double gpsSpeed, double obdSpeed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.engineLoad = engineLoad;
        this.coolantTemperature = coolantTemperature;
        this.engineRpm = engineRpm;
        this.gpsSpeed = gpsSpeed;
        this.obdSpeed = obdSpeed;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getEngineLoad() {
        return engineLoad;
    }

    public void setEngineLoad(double engineLoad) {
        this.engineLoad = engineLoad;
    }

    public double getCoolantTemperature() {
        return coolantTemperature;
    }

    public void setCoolantTemperature(double coolantTemperature) {
        this.coolantTemperature = coolantTemperature;
    }

    public double getEngineRpm() {
        return engineRpm;
    }

    public void setEngineRpm(double engineRpm) {
        this.engineRpm = engineRpm;
    }

    public double getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(double gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public double getObdSpeed() {
        return obdSpeed;
    }

    public void setObdSpeed(double obdSpeed) {
        this.obdSpeed = obdSpeed;
    }
}
