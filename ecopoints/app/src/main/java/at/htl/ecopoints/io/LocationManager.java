package at.htl.ecopoints.io;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.ecopoints.MainActivity;
import at.htl.ecopoints.model.Store;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class LocationManager {
    private static final String TAG = LocationManager.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationClient = null;
    @Inject
    Store store;

    //TODO: Need a context for the location, but the context is not available in the constructor

    @ApplicationContext
    Context context;

    @Inject
    public LocationManager() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request permissions if not granted
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, location -> {
                    if (location != null) {
                        Log.d(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                        store.next(model -> {
                            model.tripViewModel.carData.setLatitude(location.getLatitude());
                            model.tripViewModel.carData.setLongitude(location.getLongitude());
                        });
                    }
                });
    }
}
