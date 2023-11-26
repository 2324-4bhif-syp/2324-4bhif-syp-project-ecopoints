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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val htlLeonding = LatLng(48.270270270, 14.2656899796)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(htlLeonding, 10f)
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
                            state = MarkerState(position = htlLeonding),
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
}