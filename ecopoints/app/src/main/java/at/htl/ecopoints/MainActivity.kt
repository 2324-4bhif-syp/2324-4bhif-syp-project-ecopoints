package at.htl.ecopoints

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.service.TankerkoenigApiClient
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    private val latLngList = mutableStateListOf<Pair<Color, Pair<LatLng, Double>>>()

    //sample data for latLngList for a polyline (different latlngs with different colors)
    init {
        latLngList.add(Pair(Color.Red, Pair(LatLng(49.0, 14.285830), 0.0)))
        latLngList.add(Pair(Color.Blue, Pair(LatLng(49.1, 14.285830), 0.0)))
        latLngList.add(Pair(Color.Green, Pair(LatLng(49.2, 14.285830), 0.0)))
        latLngList.add(Pair(Color.Yellow, Pair(LatLng(49.3, 14.285830), 0.0)))
        latLngList.add(Pair(Color.Cyan, Pair(LatLng(49.4, 14.285830), 0.0)))
    }


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    ShowPrices()

                    ShowPhoto()
                    ShowText()

                    LastTrips()

                    val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Home") }
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){

                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@MainActivity
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ShowPhoto() {

        val painter = painterResource(id = R.drawable.app_icon)

        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .scale(2.5f)
                    .padding(top = 50.dp)
            )
        }
    }

    @Composable
    fun ShowPrices() {
        var dieselPrice = 0.0;
        var e5Price = 0.0;

        val tankerkoenigApiClient = TankerkoenigApiClient()
        try {
            thread {
            val gasData = tankerkoenigApiClient.getApiData()
                dieselPrice = gasData.diesel
                e5Price = gasData.e5
            }

            while(dieselPrice == 0.0 && e5Price == 0.0) {
                Thread.sleep(0.1.toLong())
            }

        }catch (e: Exception) {
            Log.e("Tankpreis Error", "Error: ${e.message}")
        }

        Log.i("Tankpreis", "Diesel: ${dieselPrice}")
        Log.i("Tankpreis", "E5: ${e5Price}")

        Text(
            text = "Diesel\n" + dieselPrice.toString() + "€",
            fontSize = 25.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(60.dp, 240.dp, 0.dp,0.dp),

            )

        Text(
            text = "Benzin\n" + e5Price.toString() + "€",
            fontSize = 25.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(250.dp, 240.dp, 0.dp,0.dp),

            )
    }

    @Composable
    fun ShowText(){
        val gradientColors = listOf(Gray, Green, DarkGray)

        Text(
            text = "Last Rides:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(10.dp, 340.dp, 0.dp,0.dp),

            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
        )
    }

    @Composable
    fun LastTrips() {
        var showDialog by remember { mutableStateOf(false) }
        var selectedTripDate by remember { mutableStateOf<Date?>(null) }
        val gradientColors = listOf(White, Color(0xFF43A047), Color(0xFF66BB6A), Green, White)


        val trips = listOf(
            Trip(
                id = UUID.randomUUID(),
                distance = 96.3,
                avgSpeed = 60.0,
                avgEngineRotation = 1500.0,
                date = Date(System.currentTimeMillis() - 26300060),
                rewardedEcoPoints = 10.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 75.4,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 56400000),
                rewardedEcoPoints = 8.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 60.2,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 66400000),
                rewardedEcoPoints = 8.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 96.3,
                avgSpeed = 60.0,
                avgEngineRotation = 1500.0,
                date = Date(System.currentTimeMillis() - 26300060),
                rewardedEcoPoints = 10.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 75.4,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 56400000),
                rewardedEcoPoints = 8.0
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 360.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            trips.forEach { trip ->
                Button(
                    onClick = {
                        showDialog = true
                        selectedTripDate = trip.date
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = gradientColors
                            )
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Transparent,
                        contentColor = Black
                    )
                ) {

                    val formattedDate = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(trip.date)
                    Text(formattedDate + " Uhr.  " + trip.rewardedEcoPoints.toString() + " EP")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                        selectedTripDate = null
                    },
                    title = {
                        Text(
                            selectedTripDate?.let {
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                            } ?: "Unknown Date"
                        )
                    },
                    text = {
                        ShowMap(
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(LatLng(48.306940, 14.285830), 10f)
                            })
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            selectedTripDate = null
                        }) {
                            Text("OK")
                        }
                    },
                )
            }
        }
    }
    
    @Composable
    fun ShowMap(cameraPositionState: CameraPositionState){
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cameraPositionState = cameraPositionState,
        ) {
            DrawPolyLine();
        }
    }

    @Composable
    fun DrawPolyLine() {
        for (i in 0 until latLngList.size - 1) {
            Polyline(
                points = listOf(
                    latLngList[i].second.first,
                    latLngList[i + 1].second.first
                ),
                color = latLngList[i].first,
                width = 10f
            )
        }
    }
}
