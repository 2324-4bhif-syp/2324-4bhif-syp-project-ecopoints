package at.htl.ecopoints

import android.Manifest
import android.content.ContentProviderClient
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources.Theme
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private var lastLocation: Location? = null
    private var totalDistance: Float = 0.0f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2500
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        setContent {
            EcoPointsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    sensorReading()
                    locationTest()
                    travelDistance(distance = remember { mutableStateOf(totalDistance) })
                }
            }
        }
    }

    fun onLocationChanged(location: Location) {
        if (lastLocation != null) {
            // Calculate the distance between the current and previous locations
            val distance = lastLocation!!.distanceTo(location)
            totalDistance += distance
        }
        lastLocation = location
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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

    @Composable
    fun travelDistance(distance: MutableState<Float>){
        Text(
            text = "Travelled Distance: ${distance.value} m",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(0.dp, 250.dp, 0.dp, 0.dp)
        )
    }
}

@Composable
fun sensorReading() {
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var sensorX by remember { mutableStateOf("x") }
    var sensorY by remember { mutableStateOf("y") }
    var sensorZ by remember { mutableStateOf("z") }

    var sensorXMax by remember { mutableStateOf("") }
    var sensorYMax by remember { mutableStateOf("") }
    var sensorZMax by remember { mutableStateOf("") }


    showAccelerometerReading(sensorX = sensorX, sensorY = sensorY, sensorZ = sensorZ, sensorXMax = sensorXMax, sensorYMax = sensorYMax, sensorZMax = sensorZMax)

    val sensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            // Check if the sensor type is accelerometer
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0].toString()
                val y = event.values[1].toString()
                val z = event.values[2].toString()

                if(x > sensorXMax) {
                    sensorXMax = x
                }
                if(y > sensorYMax) {
                    sensorYMax = y
                }
                if(z > sensorZMax) {
                    sensorZMax = z
                }
                sensorX = x
                sensorY = y
                sensorZ = z
            }
        }
    }


    LaunchedEffect(sensorManager) {
        sensorManager.registerListener(sensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    Text(
        text = "Accelerometer-Sensor Value:",
        style = TextStyle(fontSize = 20.sp)
    )
}

var locationManager: LocationManager? = null
var locationListener: LocationListener? = null
var speed : Float = 0.0f
var isGPSEnabled : Boolean? = false
var currentSpeed : Long = 0L
var kmphSpeed:kotlin.Double = 0.0
@Composable
fun locationTest(){

    val context = LocalContext.current
    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val permission2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

    if(permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(
            context as MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            1
        )
    }
    locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER);

    Text(text = "GPS: $isGPSEnabled" , style = TextStyle(fontSize = 20.sp), modifier = Modifier.padding(0.dp, 200.dp, 0.dp, 0.dp))

//    var location : Location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location
//
//    speed = location.getSpeed();
//    currentSpeed = round(speed as Double);
//    kmphSpeed = currentSpeed*3.6
//
//    Text(text = "Speed: $kmphSpeed kmh" , style = TextStyle(fontSize = 20.sp), modifier = Modifier.padding(0.dp, 250.dp, 0.dp, 0.dp))
}

@Composable
fun showAccelerometerReading(sensorX: String, sensorY: String, sensorZ: String, sensorXMax : String, sensorYMax : String, sensorZMax : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "X: $sensorX",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Y: $sensorY",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Z: $sensorZ",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        Column {
            Text(
                text = "XMax: $sensorXMax",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "YMax: $sensorYMax",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "ZMax: $sensorZMax",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

