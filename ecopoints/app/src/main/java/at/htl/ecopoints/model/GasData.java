package at.htl.ecopoints.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GasData {
    @JsonProperty("e5")
    private double e5;

    @JsonProperty("diesel")
    private double diesel;

    public void setE5(double e5) {
        this.e5 = e5;
    }

    public void setDiesel(double diesel) {
        this.diesel = diesel;
    }

    public double getE5() {
        return e5;
    }

    public double getDiesel() {
        return diesel;
    }
}