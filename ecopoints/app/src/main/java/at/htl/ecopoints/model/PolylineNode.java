package at.htl.ecopoints.model;

import com.google.android.gms.maps.model.LatLng;

public class PolylineNode {
    private int color;
    private Double latitude;
    private Double longitude;
    private Double fuelCons;

    public PolylineNode() {
    }

    public PolylineNode(int color, Double latitude, Double longitude, Double fuelCons) {
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fuelCons = fuelCons;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getFuelCons() {
        return fuelCons;
    }

    public void setFuelCons(Double fuelCons) {
        this.fuelCons = fuelCons;
    }
}
