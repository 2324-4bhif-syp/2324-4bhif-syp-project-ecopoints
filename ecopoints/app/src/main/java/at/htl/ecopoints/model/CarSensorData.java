package at.htl.ecopoints.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class CarSensorData {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("carData")
    private CarDataBackend carData;

    public CarSensorData() {
    }

    public CarSensorData(String timestamp, CarDataBackend carData) {
        this.timestamp = timestamp;
        this.carData = carData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public CarDataBackend getCarData() {
        return carData;
    }

    public void setCarData(CarDataBackend carData) {
        this.carData = carData;
    }
}