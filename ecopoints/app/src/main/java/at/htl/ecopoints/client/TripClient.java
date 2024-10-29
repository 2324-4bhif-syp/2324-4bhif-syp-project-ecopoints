package at.htl.ecopoints.client;

import at.htl.ecopoints.model.CarData;
import at.htl.ecopoints.model.Trip;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Path("/api/CarSensor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TripClient {
    @POST
    @Path("/log")
    CompletableFuture<String> logTripData(Trip trip);

    @POST
    @Path("/create-trip")
    CompletableFuture<String> createTrip();

    @GET
    @Path("/trips")
    CompletableFuture<List<String>> getAllTrips();

    @GET
    @Path("/trip/{tripId}")
    CompletableFuture<List<CarData>> getTripData(@PathParam("tripId") UUID tripId);

    @POST
    @Path("/trip/{tripId}/data")
    CompletableFuture<String> addDataToTrip(@PathParam("tripId") UUID tripId, List<CarData> sensorData);
}
