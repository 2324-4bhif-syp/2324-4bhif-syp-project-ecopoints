package at.htl.ecopoints.model;

import java.util.Date;

public class Location {
    public double latitude;
    public double longitude;
    public Date date;

    public Location(double latitude, double longitude, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public Location() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
