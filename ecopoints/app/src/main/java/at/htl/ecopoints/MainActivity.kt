package at.htl.ecopoints

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.service.CarAPI
import at.htl.ecopoints.service.TankerkoenigApiClient
import at.htl.ecopoints.service.TripAdapter
import at.htl.ecopoints.service.VINNumberAPI
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var showDialog: Boolean by remember { mutableStateOf(false) }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    ShowPrices()

                    ShowPhoto()
                    ShowText()
                    //ShowTrips(context = this, activity = this@MainActivity)

                    MyScreenContent()

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
    fun MyScreenContent() {
        var showDialog by remember { mutableStateOf(false) }
        var selectedTripDate by remember { mutableStateOf<Date?>(null) }

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
            modifier = Modifier.fillMaxSize().padding(0.dp, 360.dp, 0.dp, 0.dp),
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
                        ) },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            selectedTripDate = null
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
