package at.htl.ecopoints.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.interfaces.OnLocationChangedListener
import at.htl.ecopoints.service.TestLocationService
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
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
            val currentLocation = LatLng(latitude, longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
            }
            var mapProperties by remember {
                mutableStateOf(MapProperties(mapType = MapType.NORMAL))
            }
            EcoPointsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties
                    ) {
                        Marker(
                            state = MarkerState(position = currentLocation),
                            title = "Htl Leonding",
                            snippet = "Marker in Leonding"
                        )
                    }
                    Column {
                        MapTypeControls(onMapTypeClick = {
                            Log.d("GoogleMap", "Selected map type $it")
                            mapProperties = mapProperties.copy(mapType = it)
                        })
                    }
                }
            }
        }
    }

    @Composable
    private fun MapTypeControls(
        onMapTypeClick: (MapType) -> Unit
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(state = ScrollState(0)),
            horizontalArrangement = Arrangement.Center
        ) {
            MapType.values().forEach {
                MapTypeButton(type = it) { onMapTypeClick(it) }
            }
        }
    }

    @Composable
    private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
        MapButton(text = type.toString(), onClick = onClick)

    @Composable
    private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
        Button(
            modifier = modifier.padding(4.dp),
            onClick = onClick
        ) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }
    override fun onLocationChanged(lat: String, lon: String) {
        longitude = lon.toDouble()
        latitude = lat.toDouble()
    }
}