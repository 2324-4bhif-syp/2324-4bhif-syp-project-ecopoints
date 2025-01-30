package at.htl.ecopoints;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.compose.foundation.interaction.DragInteraction;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import javax.inject.Inject;

import at.htl.ecopoints.model.Store;
import at.htl.ecopoints.ui.layout.TripView;
import at.htl.ecopoints.util.ConfigLoader;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends ComponentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    TripView tripView;

    @Inject
    Store store;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");

        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        super.onCreate(savedInstanceState);
        tripView.compose(this);
        checkPermissions();

        ConfigLoader.init(this);
    }

    private void checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission" + permission, "NOT granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{permission}, 1);
        }else{
            // if the permissions have already been granted do the following
            Log.d("permission" + permission, "granted");
        }
    }

    private void checkPermissions(){

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_CONNECT};

        for (String permission : permissions) {
           checkPermission(permission);
        }
    }
}
