package at.htl.ecopoints.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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
                        Text(text = "Connected Device Information", fontWeight = FontWeight.Bold)
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
        var coolantTemp by remember { mutableStateOf("0") }
        var buttonClicked by remember { mutableStateOf(false) }

        Button(onClick = { buttonClicked = true }) {
            Text("Read with EltonLib")
        }

        LaunchedEffect(buttonClicked) {
            if (buttonClicked) {
                rpm = getRpm(service)
                speed = service.getEltonApiSpeed()
                coolantTemp = service.getCoolantTemp()
                buttonClicked = false
            }
        }

        Text(text = "Current-Speed $speed")
        Text(text = "Current-Rpm $rpm")
        Text(text = "Coolat-Temp $coolantTemp")
    }

    @Composable
    private fun TestReadCustomComm(){
        var rpm by remember { mutableStateOf("0") }
        var speed by remember { mutableStateOf("0") }
        var coolantTemp by remember { mutableStateOf("0") }
        var buttonClicked by remember { mutableStateOf(false) }

        var service = Obd2Service(deviceAddress)

        service.initOBD()

        Button(onClick = { buttonClicked = true }) {
            Text("Read with Custom Comm")
        }

        LaunchedEffect(buttonClicked) {
            if (buttonClicked) {
                rpm = service.getRPM()
                speed = service.getSpeed()
                coolantTemp = service.getCoolantTemp()
                buttonClicked = false
            }
        }

        Text(text = "Current-Speed $speed")
        Text(text = "Current-Rpm $rpm")
        Text(text = "Coolat-Temp $coolantTemp")
    }

    private suspend fun getRpm(service: Obd2Service): String {
        return withContext(Dispatchers.IO) {
            service.getEltonApiRPM()
        }
    }

    private suspend fun getSpeed(service: Obd2Service): String {
        return withContext(Dispatchers.IO) {
            service.getEltonApiSpeed()
        }
    }

    private suspend fun getLoad(service: Obd2Service): String {
        return withContext(Dispatchers.IO) {
            service.getEltonApiLoad()
        }
    }
}