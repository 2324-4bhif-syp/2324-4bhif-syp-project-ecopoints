package at.htl.ecopoints.model;

public class FuelType {
    private String name;
    private boolean isSelected;

    public FuelType() {
    }

    public FuelType(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
