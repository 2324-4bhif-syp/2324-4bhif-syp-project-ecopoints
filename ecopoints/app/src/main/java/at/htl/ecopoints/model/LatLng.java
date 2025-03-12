package at.htl.ecopoints.model;

public class LatLng {
    private double item1;
    private double item2;

    public LatLng() {} // Default constructor needed for JSON parsing

    public LatLng(double item1, double item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public double getItem1() {
        return item1;
    }

    public void setItem1(double item1) {
        this.item1 = item1;
    }

    public double getItem2() {
        return item2;
    }

    public void setItem2(double item2) {
        this.item2 = item2;
    }
}