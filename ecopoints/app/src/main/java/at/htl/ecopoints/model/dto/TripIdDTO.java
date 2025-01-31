package at.htl.ecopoints.model.dto;

import java.util.UUID;

public class TripIdDTO {
    private UUID tripId;

    public TripIdDTO(UUID tripId) {
        this.tripId = tripId;
    }

    public TripIdDTO() {
    }

    public UUID getTripId() {
        return tripId;
    }

    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }
}
