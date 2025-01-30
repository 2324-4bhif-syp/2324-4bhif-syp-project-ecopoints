package at.htl.ecopoints;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import javax.inject.Inject;

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

        Log.i(TAG, "Getting all trips");

        this.tripService.getAllTrips().thenAccept(trips -> {
            Log.i(TAG, "Trips: " + trips);
        });

        homeView.compose(this);
    }
}
