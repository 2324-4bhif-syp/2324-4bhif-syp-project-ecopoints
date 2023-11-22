package at.htl.ecopoints.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.service.Obd2Service
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import kotlinx.coroutines.delay


class Obd2ReadingActivity : ComponentActivity() {
    private var deviceName = ""
    private var deviceAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceName = intent.getStringExtra("deviceName") ?: ""
        deviceAddress = intent.getStringExtra("deviceAddress") ?: ""

        setContent {
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(text = "Device Information", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Name: $deviceName")
                        Text(text = "Address: $deviceAddress")
                        TestReadEltonLib()
                        TestReadCustomComm()
                    }
                }
            }
        }
    }



    @Composable
    private fun TestReadEltonLib() {
        Log.d("Obd2ReadingActivity", deviceAddress)
        val service = Obd2Service(deviceAddress)
        var rpm by remember { mutableStateOf("0") }
        var speed by remember { mutableStateOf("0") }
        var load by remember { mutableStateOf("0") }
        var buttonClicked by remember { mutableStateOf(false) }

        Button(onClick = { buttonClicked = true }) {
            Text("Read with Custom Comm V2")
        }

        LaunchedEffect(buttonClicked) {
            if (buttonClicked) {
                for (i in 0..1000) {
                    delay(500)
                    rpm = service.getRPM()
                    speed = service.getSpeed()
                    load = service.getCoolantTemp()
                }
                buttonClicked = false
            }
        }

        Text(text = "Current-Speed $speed")
        Text(text = "Current-Rpm $rpm")
        Text(text = "Coolant-Load $load")
    }

    @Composable
    private fun TestReadCustomComm() {
        var rpm by remember { mutableStateOf("0") }
        var speed by remember { mutableStateOf("0") }
        var coolantTemp by remember { mutableStateOf("0") }
        var buttonClicked by remember { mutableStateOf(false) }

        val service = Obd2Service(deviceAddress)


        Button(onClick = { buttonClicked = true }) {
            Text("Read with Custom Comm V1")
        }

        LaunchedEffect(buttonClicked) {
            if (buttonClicked) {
                service.initOBD()
                for (i in 0..1000) {
                    var rpm1 = service.getRPM()
                    var speed2 = "0"//service.getSpeed()
                    var coolantTemp3 = "0"//service.getCoolantTemp()

                    if (rpm1 != "0") {
                        rpm = rpm1
                    }
                    if (speed2 != "0") {
                        speed = speed2
                    }
                    if (coolantTemp3 != "0") {
                        coolantTemp = coolantTemp3
                    }
                    delay(500)
                }
                buttonClicked = false
            }
        }

        Text(text = "Current-Speed $speed")
        Text(text = "Current-Rpm $rpm")
        Text(text = "Coolant-Temp $coolantTemp")
    }
}