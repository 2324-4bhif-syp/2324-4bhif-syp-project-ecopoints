package at.htl.ecopoints.service;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import at.htl.ecopoints.client.TripClient;
import at.htl.ecopoints.model.CarData;
import at.htl.ecopoints.model.CarSensorData;
import at.htl.ecopoints.model.Trip;
import at.htl.ecopoints.model.dto.TripIdDTO;
import at.htl.ecopoints.model.dto.TripMetaData;
import at.htl.ecopoints.util.ConfigLoader;
import at.htl.ecopoints.util.resteasy.RestApiClientBuilder;


@Singleton
public class TripService {
    static final String TAG = TripService.class.getSimpleName();
    private final TripClient tripClient;
    private static final String BASE_URL = ConfigLoader.getBaseUrl();

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

    public CompletableFuture<TripIdDTO> createTrip() {
        return CompletableFuture.supplyAsync(tripClient::createTrip);
//                .thenApply(response -> "Trip created with ID: " + response.getTripId())
//                .exceptionally(e -> "Failed to create trip: " + e.getMessage() + e.toString());;

    }

    public CompletableFuture<List<String>> getAllTripIds() {
        return CompletableFuture.supplyAsync(tripClient::getAllTripIds);
    }

    public CompletableFuture<List<TripMetaData>> getAllTrips() {
        return CompletableFuture.supplyAsync(tripClient::getAllTrips);
    }

    public CompletableFuture<List<CarData>> getTripData(UUID tripId) {
        return CompletableFuture.supplyAsync(() -> tripClient.getTripData(tripId));
    }

    public CompletableFuture<String> addDataToTrip(UUID tripId, List<CarSensorData> sensorData) {
        return CompletableFuture.supplyAsync(() -> tripClient.addDataToTrip(tripId, sensorData))
                .thenApply(response -> "Data added successfully")
                .exceptionally(e -> "Failed to add data: " + e.getMessage());
    }
}
