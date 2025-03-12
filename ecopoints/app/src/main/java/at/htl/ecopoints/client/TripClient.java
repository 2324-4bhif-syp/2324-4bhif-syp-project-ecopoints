package at.htl.ecopoints.client;

import at.htl.ecopoints.model.CarData;
import at.htl.ecopoints.model.CarSensorData;
import at.htl.ecopoints.model.Trip;
import at.htl.ecopoints.model.dto.TripIdDTO;
import at.htl.ecopoints.model.dto.TripMetaData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TripClient {
    @POST
    @Path("/log")
    String logTripData(Trip trip);

    @POST
    @Path("/create-trip")
    TripIdDTO createTrip();

    @GET
    @Path("/tripIds")
    List<String> getAllTripIds();

    @GET
    @Path("/trips")
    TripMetaData[] getAllTrips();

    @GET
    @Path("/trip/{tripId}")
    List<CarData> getTripData(@PathParam("tripId") UUID tripId);

    @POST
    @Path("/trip/{tripId}/data")
    String addDataToTrip(@PathParam("tripId") UUID tripId, List<CarSensorData> sensorData);
}
