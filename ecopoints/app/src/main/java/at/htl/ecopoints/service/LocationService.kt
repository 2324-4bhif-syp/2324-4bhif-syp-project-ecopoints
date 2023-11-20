package at.htl.ecopoints.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

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