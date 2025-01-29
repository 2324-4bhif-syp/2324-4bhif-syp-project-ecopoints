package at.htl.ecopoints.service;

import android.util.Log;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import at.htl.ecopoints.client.TripClient;
import at.htl.ecopoints.model.CarData;
import at.htl.ecopoints.model.Trip;
import at.htl.ecopoints.util.resteasy.RestApiClientBuilder;

@Singleton
public class TripService {
    static final String TAG = TripService.class.getSimpleName();
    private final TripClient tripClient;
    public static String BASE_URL = "https://if180123.cloud.htl-leonding.ac.at";

    @Inject
    public TripService(RestApiClientBuilder builder) {
        Log.i(TAG, "Creating TripService with base URL: " + BASE_URL);
        this.tripClient = builder.build(TripClient.class, BASE_URL);
    }

    public CompletableFuture<String> logTripData(Trip trip) {
        return CompletableFuture.supplyAsync(() -> tripClient.logTripData(trip))
                .thenApply(response -> "Trip logged: " + response)
                .exceptionally(e -> "Failed to log trip: " + e.getMessage());
    }

    public CompletableFuture<String> createTrip() {
        return CompletableFuture.supplyAsync(tripClient::createTrip)
                .thenApply(response -> "Trip created with ID: " + response)
                .exceptionally(e -> "Failed to create trip: " + e.getMessage());
    }

    public CompletableFuture<List<String>> getAllTrips() {
        return CompletableFuture.supplyAsync(() -> (List<String>) tripClient.getAllTrips());
    }

    public CompletableFuture<List<CarData>> getTripData(UUID tripId) {
        return CompletableFuture.supplyAsync(() -> (List<CarData>) tripClient.getTripData(tripId));
    }

    public CompletableFuture<String> addDataToTrip(UUID tripId, List<CarData> sensorData) {
        return CompletableFuture.supplyAsync(() -> tripClient.addDataToTrip(tripId, sensorData))
                .thenApply(response -> "Data added successfully")
                .exceptionally(e -> "Failed to add data: " + e.getMessage());
    }
}
