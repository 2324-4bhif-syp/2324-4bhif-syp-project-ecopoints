package at.htl.ecopoints.service

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationService(private val onLocationChanged: (Location) -> Unit): LocationListener {
    private var lastLocation: Location? = null

    override fun onLocationChanged(location: Location) {
        if (lastLocation != null) {
            val distance = lastLocation!!.distanceTo(location)

            if (distance > 1.0) {
                onLocationChanged(location)
            }
        }
        lastLocation = location
    }

    fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient, locationRequest: com.google.android.gms.location.LocationRequest) {
        //fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { onLocationChanged(it) }
        }
    }

    fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}