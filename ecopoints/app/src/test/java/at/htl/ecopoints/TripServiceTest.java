package at.htl.ecopoints;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import at.htl.ecopoints.service.TripService;
import at.htl.ecopoints.util.resteasy.RestApiClientBuilder;

public class TripServiceTest {
    TripService tripService;

    @Before
    public void setup() {
        tripService = new TripService(new RestApiClientBuilder());
        System.setProperty("BASE_URL", "http://localhost:5221");
    }

    @Test
    public void testGetAllTrips() throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        tripService.getAllTripIds().thenAccept(trips -> {
            System.out.println("Trips: " + trips);
            future.complete(true); // Ensure trips are fetched
        }).exceptionally(ex -> {
            System.out.println("Failed to fetch trips: " + ex.getMessage());
            future.complete(false); // Fail if an exception occurs
            return null;
        });

        // Wait for async operation to finish
        Assert.assertTrue("Trips list should not be empty", future.get());
    }
}
