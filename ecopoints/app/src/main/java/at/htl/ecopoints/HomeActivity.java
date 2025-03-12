package at.htl.ecopoints;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import at.htl.ecopoints.model.CarDataBackend;
import at.htl.ecopoints.model.CarSensorData;
import at.htl.ecopoints.service.TripService;
import at.htl.ecopoints.ui.layout.HomeView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends ComponentActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Inject
    HomeView homeView;

    @Inject
    TripService tripService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        List<CarSensorData> carSensorDataList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            CarDataBackend carData = new CarDataBackend(
                    48.2082 + Math.random(),  // Beispielwerte für Latitude
                    16.3738 + Math.random(),  // Beispielwerte für Longitude
                    200 + Math.random() * 50, // Altitude zwischen 200m und 250m
                    Math.random() * 100, // Engine Load 0 - 100%
                    80 + Math.random() * 20, // Coolant Temp 80 - 100°C
                    1500 + Math.random() * 2000, // RPM zwischen 1500 und 3500
                    60 + Math.random() * 40, // GPS Speed zwischen 60 und 100 km/h
                    60 + Math.random() * 40  // OBD Speed ähnlich GPS Speed
            );

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Instant instant = timestamp.toInstant();
            String formattedTimestamp = instant.atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_INSTANT);
            CarSensorData sensorData = new CarSensorData(formattedTimestamp, carData);
            carSensorDataList.add(sensorData);
        }

        tripService.createTrip().thenAccept(tripIdDTO -> {
            Log.i(TAG, "Created trip with ID: " + tripIdDTO.getTripId());
            tripService.addDataToTrip(tripIdDTO.getTripId(), carSensorDataList).thenAccept(response -> {
                Log.i(TAG, "Added data to trip: " + response);
            });
        });


        List<String>  tripIds = new ArrayList<>();

        tripService.getAllTrips().thenAccept(trips -> {
            tripIds.addAll(trips);
            Log.i(TAG, "All trips: " + trips);
        });

        homeView.compose(this, tripIds);
    }
}
