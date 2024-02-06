package at.htl.ecopoints.model;

public class FuelType {
    private String name;
    private boolean isSelected;

    public FuelType(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public boolean getSelection() {
        return isSelected;
    }
}
