package at.htl.ecopoints.model;

public class BtDevice {
    private String name = "no device";
    private String address = "0";

    public BtDevice() {
    }

    public BtDevice(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
