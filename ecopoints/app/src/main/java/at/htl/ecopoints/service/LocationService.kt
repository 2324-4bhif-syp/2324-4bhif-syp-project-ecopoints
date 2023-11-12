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

    fun startLocationUpdates(context: Context ,fusedLocationClient: FusedLocationProviderClient, locationRequest: com.google.android.gms.location.LocationRequest) {

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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { onLocationChanged(it) }
        }
    }

    fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @Composable
    fun printTravelDistance(totalDistance: Float){
        val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.GERMAN))
        Text(
            text = "Travelled Distance: ${decimalFormat.format(totalDistance)} m",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(0.dp, 280.dp, 0.dp, 0.dp)
        )
    }
}