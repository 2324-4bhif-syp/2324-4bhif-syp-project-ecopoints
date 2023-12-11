//package at.htl.ecopoints.activity
//
//import kotlin.math.*
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.ScrollState
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material.*
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import at.htl.ecopoints.interfaces.OnLocationChangedListener
//import at.htl.ecopoints.service.TestLocationService
//import at.htl.ecopoints.ui.theme.EcoPointsTheme
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.unit.dp
//import at.htl.ecopoints.ui.theme.EcoPointsTheme
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.maps.android.compose.CameraPositionState
//import com.google.maps.android.compose.GoogleMap
//import com.google.maps.android.compose.MapProperties
//import com.google.maps.android.compose.MapType
//import com.google.maps.android.compose.MapUiSettings
//import com.google.maps.android.compose.Marker
//import com.google.maps.android.compose.MarkerState
//import com.google.maps.android.compose.Polyline
//import com.google.maps.android.compose.rememberCameraPositionState
//import kotlinx.coroutines.launch
//
//class MapActivity : ComponentActivity(), OnLocationChangedListener {
//
//    private val testLocationService: TestLocationService by lazy {
//        TestLocationService(this)
//    }
//    private var longitude = 14.285830
//    private var latitude = 48.306940
//    private var latLngHasChanged = mutableStateOf(false)
//
//    private val latLngList =
//        mutableStateListOf<Pair<Color, Pair<LatLng, Double>>>()
//
//    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        testLocationService.setOnLocationChangedListener(this)
//
//        setContent {
//            val currentLocation = LatLng(latitude, longitude)
//            val cameraPositionState = rememberCameraPositionState {
//                position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
//            }
//            var mapProperties by remember {
//                mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))
//            }
//            EcoPointsTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Scaffold(topBar = {
//                        TopAppBar(backgroundColor = Color.White, title = {
//                            Column(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalAlignment = Alignment.End
//                            ) {
//                                MapTypeControls(onMapTypeClick = {
//                                    Log.d("GoogleMap", "Selected map type $it")
//                                    mapProperties = mapProperties.copy(mapType = it)
//                                })
//                            }
//                        })
//                    }) {
//                        GoogleMap(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(300.dp),
//                            cameraPositionState = cameraPositionState,
//                            properties = mapProperties
//                        ) {
//                            DrawPolyline()
//                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
//                                LatLng(latitude, longitude),
//                                10f
//                            )
//                        }
//                    }
//                }
//            }
//
//        setContent {
//            val htlLeonding = LatLng(48.270270270, 14.2656899796)
//            val cameraPositionState = rememberCameraPositionState {
//                position = CameraPosition.fromLatLngZoom(htlLeonding, 10f)
//            }
//            var mapProperties by remember {
//                mutableStateOf(MapProperties(mapType = MapType.NORMAL))
//            }
//            EcoPointsTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    GoogleMap(
//                        modifier = Modifier.fillMaxSize(),
//                        cameraPositionState = cameraPositionState,
//                        properties = mapProperties
//                    ) {
//                        Marker(
//                            state = MarkerState(position = htlLeonding),
//                            title = "Htl Leonding",
//                            snippet = "Marker in Leonding"
//                        )
//                    }
//                    Column {
//                        MapTypeControls(onMapTypeClick = {
//                            Log.d("GoogleMap", "Selected map type $it")
//                            mapProperties = mapProperties.copy(mapType = it)
//                        })
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    private fun DrawPolyline() {
//        if (!latLngHasChanged.value) {
//            for(i in 0 until latLngList.size - 1) {
//                Polyline(
//                    points = listOf(latLngList[i].second.first, latLngList[i + 1].second.first),
//                    color = latLngList[i].first,
//                    width = 10f
//                )
//            }
//        }
//        latLngHasChanged.value = false
//    }
//
//    @Composable
//    private fun MapTypeControls(
//        onMapTypeClick: (MapType) -> Unit
//    ) {
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(state = ScrollState(0)),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            MapType.values().forEach {
//                MapTypeButton(type = it) { onMapTypeClick(it) }
//            }
//        }
//    private fun MapTypeControls(
//        onMapTypeClick: (MapType) -> Unit
//    ) {
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(state = ScrollState(0)),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            MapType.values().forEach {
//                MapTypeButton(type = it) { onMapTypeClick(it) }
//            }
//        }
//    }
//
//    @Composable
//    private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
//        MapButton(text = type.toString(), onClick = onClick)
//
//    @Composable
//    private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
//        Button(
//            modifier = modifier.padding(4.dp),
//            onClick = onClick
//        ) {
//            Text(text = text, style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//
//    override fun onLocationChanged(lat: String, lon: String) {
//        val newLatitude = lat.toDouble()
//        val newLongitude = lon.toDouble()
//
//        if (isLocationChanged(newLatitude, newLongitude, latitude, longitude, 1.0)) {
//            latitude = newLatitude
//            longitude = newLongitude
//
//            Log.d("MapTracking", "Location changed to $latitude, $longitude")
//            addItemToList(LatLng(latitude, longitude))
//        }
//    }
//
//    private fun addItemToList(newItem: LatLng) {
//        val fuelCons = generateRandomFuelCons()
//        val color = when {
//            fuelCons <= 6.0 -> Color.Green
//            fuelCons > 6.0 && fuelCons <= 12 -> Color.Yellow
//            fuelCons > 12 && fuelCons <= 20 -> Color.Red
//            else -> Color.Black
//        }
//        latLngList.add(Pair(color, Pair(newItem, fuelCons)))
//        latLngHasChanged.value = true
//
//        Log.d("FuelTracking", "Fuel consumption: $fuelCons")
//    }
//
//    //for testing purposes, remove if database is set up
//    private fun generateRandomFuelCons(): Double {
//        return (3..21).random().toDouble()
//    }
//
//    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val R = 6371
//        val dLat = Math.toRadians(lat2 - lat1)
//        val dLon = Math.toRadians(lon2 - lon1)
//        val a = sin(dLat / 2) * sin(dLat / 2) +
//                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
//                sin(dLon / 2) * sin(dLon / 2)
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//        return R * c * 1000
//    }
//
//    private fun isLocationChanged(newLat: Double, newLon: Double, oldLat: Double, oldLon: Double, threshold: Double): Boolean {
//        val distance = haversine(newLat, newLon, oldLat, oldLon)
//        return distance > threshold
//    }
//}
