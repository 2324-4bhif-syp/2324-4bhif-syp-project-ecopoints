package at.htl.ecopoints

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import at.htl.ecopoints.activity.BluetoothDeviceListActivity
//import at.htl.ecopoints.activity.MapActivity
import at.htl.ecopoints.db.CarData
import at.htl.ecopoints.db.DBHelper
import at.htl.ecopoints.activity.TripActivity
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.service.AccelerometerSensorService
import at.htl.ecopoints.service.LocationService
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var totalDistance: Float = 0.0f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private var locationService: LocationService = LocationService()
    private var locationManager: LocationManager? = null
    private var isGPSEnabled: Boolean? = false
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        val db = DBHelper(this, null)
        val carData = CarData(
            longitude = 10.0,
            latitude = 20.0,
            currentEngineRPM = 1500.0,
            currentVelocity = 60.0,
            throttlePosition = 50.0,
            engineRunTime = "2 hours",
            timestamp = Timestamp.valueOf("2023-11-29 12:30:00")
        )
        db.addCarData(carData)
        db.syncWithBackend()
         */

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }

        setContent {
            val total = remember { mutableStateOf(totalDistance) }

            LaunchedEffect(totalDistance) {
                while (true) {
                    total.value = totalDistance
                    delay(250)
                }
            }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SensorReading()
                    PrintTravelledDistance(total.value)
                    //ShowMap()
                    ShowBluetoothDevicesButton()
                    ShowTrip()

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

    fun onLocationChanged(location: Location) {
        totalDistance += locationService.getDistance(location)
    }

    private fun startLocationUpdates() {
        locationService.startLocationUpdates(
            this, fusedLocationClient, locationRequest, locationCallback
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { onLocationChanged(it) }
        }
    }

    @Composable
    fun PrintTravelledDistance(totalDistance: Float) {
        val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.GERMAN))
        Text(
            text = "Travelled Distance: ${decimalFormat.format(totalDistance)} m",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(0.dp, 280.dp, 0.dp, 0.dp)
        )
    }

    @Composable
    fun SensorReading() {

        var sensorX by remember { mutableStateOf("x") }
        var sensorY by remember { mutableStateOf("y") }
        var sensorZ by remember { mutableStateOf("z") }
        var sensorXMax by remember { mutableStateOf("") }
        var sensorYMax by remember { mutableStateOf("") }
        var sensorZMax by remember { mutableStateOf("") }

        val sensorManager =
            LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val accelerometerSensorService = AccelerometerSensorService()

        val resetSensors: () -> Unit = {
            accelerometerSensorService.resetSensors()
            sensorXMax = accelerometerSensorService.sensorXMax
            sensorYMax = accelerometerSensorService.sensorYMax
            sensorZMax = accelerometerSensorService.sensorZMax
        }

        ShowAccelerometerReading(
            sensorX, sensorY, sensorZ, sensorXMax, sensorYMax, sensorZMax
        )

        fun updateSensors() {
            sensorX = accelerometerSensorService.sensorX
            sensorY = accelerometerSensorService.sensorY
            sensorZ = accelerometerSensorService.sensorZ
        }

        fun updateMaxSensors() {
            sensorXMax = accelerometerSensorService.sensorXMax
            sensorYMax = accelerometerSensorService.sensorYMax
            sensorZMax = accelerometerSensorService.sensorZMax
        }
        ResetButton(onResetClick = resetSensors)

        val sensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerSensorService.setSensors(event)
                    updateSensors()
                    updateMaxSensors()
                }
            }
        }

        LaunchedEffect(sensorManager) {
            sensorManager.registerListener(
                sensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        Text(
            text = "Accelerometer-Sensor Value:", style = TextStyle(fontSize = 20.sp)
        )
    }

    @Composable
    fun ResetButton(onResetClick: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = onResetClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(0.dp, 160.dp, 170.dp, 0.dp)

            ) {
                Text(text = "Reset")
            }
        }
    }

    @Composable
    fun ShowAccelerometerReading(
        sensorX: String,
        sensorY: String,
        sensorZ: String,
        sensorXMax: String,
        sensorYMax: String,
        sensorZMax: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "X: $sensorX", style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ), modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Y: $sensorY", style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ), modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Z: $sensorZ", style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ), modifier = Modifier.padding(16.dp)
                )
            }
            Column {
                Text(
                    text = "XMax: $sensorXMax", style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ), modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "YMax: $sensorYMax", style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ), modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "ZMax: $sensorZMax", style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ), modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    @Composable
    fun ShowBluetoothDevicesButton() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    startActivity(
                        Intent(
                            this@MainActivity, BluetoothDeviceListActivity::class.java
                        )
                    )
                }, modifier = Modifier
                    .padding(80.dp, 500.dp, 0.dp, 0.dp) //.padding(0.dp, 160.dp, 170.dp, 0.dp)
            ) {
                Text(text = "Show Paired Bluetooth Devices")
            }
        }
    }

    /*

    @Composable
    fun ShowMap() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    startActivity(Intent(this@MainActivity, MapActivity::class.java))
                }, modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Text(text = "Show Map")
            }
        }
    }

    */


    @Composable
    fun ShowTrip() {
        Spacer(modifier = Modifier.height(100.dp))
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    startActivity(Intent(this@MainActivity, TripActivity::class.java))
                }, modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Text(text = "Show Trip")
            }
        }
    }
}
