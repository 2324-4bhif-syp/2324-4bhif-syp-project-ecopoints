package at.htl.ecopoints.db.services;

import at.htl.ecopoints.model.Trip;
import okhttp3.Response;

import java.util.List;
import java.util.UUID;

public class TripService extends Service {
    private final String endPoint = "trip";

    public Response createTrip(Trip trip) throws Exception {
        return super.create(trip, endPoint);
    }

    public Response updateTrip(Trip trip, UUID id) throws Exception {
        return super.update(trip, endPoint, id.toString());
    }

    public Response deleteTrip(UUID id) throws Exception {
        return super.delete(endPoint, id.toString());
    }

    public Trip getTripById(UUID id) throws Exception {
        return super.getById(endPoint, id.toString());
    }

    public List<Trip> getAllTrips() throws Exception {
        return super.getAll(endPoint);
    }
}
