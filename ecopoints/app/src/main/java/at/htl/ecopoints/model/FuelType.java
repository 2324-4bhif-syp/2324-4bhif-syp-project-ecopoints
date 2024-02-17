package at.htl.ecopoints.model;

import androidx.annotation.Nullable;

public class FuelType {
    private String name;

    public FuelType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof FuelType) {
            FuelType fuelType = (FuelType) obj;
            return this.name.equals(fuelType.name);
        }
        return false;
    }
}
