package at.htl.ecopoints

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity(), LocationListener {
    lateinit var distanceTextView: TextView
    lateinit var lastLocation: Location;
    var distance = 0.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoPointsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    sensorReading()
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {

        if(::lastLocation.isInitialized){
            distance += lastLocation.distanceTo(location)
            distanceTextView.text = "Meters: $distance"
        }

        lastLocation = location;
    }
}

@Composable
fun printMetres(distance: Double){
    Text(text = "Metres: " + distance)
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

