package at.htl.ecopoints.io

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import javax.inject.Singleton

@Singleton
class LocationService {
    private val TAG = this.javaClass.simpleName;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 0.0;
    private var lng = 0.0;


    @SuppressLint("MissingPermission")
    constructor(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    Log.i(TAG, "Location changed: $location")
                    lat = location.latitude
                    lng = location.longitude
                }
            }


    }

    fun createLocationRequest() {
        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
    }

    constructor() {
        Log.i(TAG, "Location changed:test")

    }

    fun getLat(): Double {
        return lat
    }

    fun getLng(): Double {
        return lng
    }
}