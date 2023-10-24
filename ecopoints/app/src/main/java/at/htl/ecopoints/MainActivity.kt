package at.htl.ecopoints

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htl.ecopoints.ui.theme.EcoPointsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoPointsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SensorReading()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcoPointsTheme {
        Greeting("Android")
    }
}



@Composable
fun SensorReading() {
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var sensorX by remember { mutableStateOf("x") }
    var sensorY by remember { mutableStateOf("y") }
    var sensorZ by remember { mutableStateOf("z") }

    var sensorXMax by remember { mutableStateOf("") }
    var sensorYMax by remember { mutableStateOf("") }
    var sensorZMax by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "X: $sensorX",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Y: $sensorY",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Z: $sensorZ",
                style = TextStyle(
                    color = Color.White,
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "YMax: $sensorYMax",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "ZMax: $sensorZMax",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }


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
        text = "Sensor Value : ",
        style = TextStyle(fontSize = 20.sp)
    )
}

