package at.htl.ecopoints.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

class LocationService {
    private var lastLocation: Location? = null

    fun getDistance(location: Location): Float {
        var distance: Float = 0.0f

        if (lastLocation != null) {
            val distanceToLocation = lastLocation!!.distanceTo(location)

            if (distanceToLocation > 1.0) {
                distance = distanceToLocation
            }
        }
        lastLocation = location
        return distance
    }

    fun startLocationUpdates(context: Context ,fusedLocationClient: FusedLocationProviderClient,
                             locationRequest: com.google.android.gms.location.LocationRequest,
                             locationCallback: LocationCallback) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}