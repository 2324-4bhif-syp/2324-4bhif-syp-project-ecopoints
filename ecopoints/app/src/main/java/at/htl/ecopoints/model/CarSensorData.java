package at.htl.ecopoints.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class CarSensorData {

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    @JsonProperty("CarData")
    private CarDataBackend carData;

    public CarSensorData() {
    }

    public CarSensorData(Timestamp timestamp, CarDataBackend carData) {
        this.timestamp = timestamp;
        this.carData = carData;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public CarDataBackend getCarData() {
        return carData;
    }

    public void setCarData(CarDataBackend carData) {
        this.carData = carData;
    }
}