package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import at.htl.ecopoints.interfaces.OnLocationChangedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class TestLocationService  {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private var locationService: LocationService = LocationService()
    private var locationManager: LocationManager? = null
    private var lat = "";
    private var lon = "";

    private lateinit var locationChangedListener: OnLocationChangedListener

    constructor(context: Context){
        fusedLocationClient = FusedLocationProviderClient(context)

        locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        startLocationUpdates()
    }

    fun setOnLocationChangedListener(listener: OnLocationChangedListener) {
        this.locationChangedListener = listener
    }


    fun onLocationChanged(location: Location) {
        lat = location.latitude.toString()
        lon = location.longitude.toString()
        locationChangedListener.onLocationChanged(lat, lon)
    }

    private fun startLocationUpdates() {
        startLocationUpdates(
            fusedLocationClient,
            locationRequest,
            locationCallback
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient,
                             locationRequest: com.google.android.gms.location.LocationRequest,
                             locationCallback: LocationCallback) {


        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}