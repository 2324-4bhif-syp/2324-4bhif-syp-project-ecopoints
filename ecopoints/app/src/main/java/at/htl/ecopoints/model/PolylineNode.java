package at.htl.ecopoints.model;

import com.google.android.gms.maps.model.LatLng;

public class PolylineNode {
    private int color;
    private Double latitude;
    private Double longitude;
    private Double fuelCons;

    public PolylineNode() {
    }

    public PolylineNode(Double latitude, Double longitude, Double fuelCons) {
        this.latitude = latitude;
        this.longitude = longitude;
        if(fuelCons == null) {
            this.fuelCons = 0.0;
        } else {
            this.fuelCons = fuelCons;
        }
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
