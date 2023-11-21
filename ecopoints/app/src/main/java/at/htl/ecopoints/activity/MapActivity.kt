package at.htl.ecopoints.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import at.htl.ecopoints.interfaces.OnLocationChangedListener
import at.htl.ecopoints.service.TestLocationService
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapActivity : ComponentActivity(), OnLocationChangedListener {

    private val testLocationService: TestLocationService by lazy {
        TestLocationService(this)
    }
    private var longitude = 1.35
    private var latitude = 103.87

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testLocationService.setOnLocationChangedListener(this)


        setContent {


            val singapore = LatLng(latitude, longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(singapore, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        };
    }

    override fun onLocationChanged(lat: String, lon: String) {
        longitude = lon.toDouble()
        latitude = lat.toDouble()
    }
}