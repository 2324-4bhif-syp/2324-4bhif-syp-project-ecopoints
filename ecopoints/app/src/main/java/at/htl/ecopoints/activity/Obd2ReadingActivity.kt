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
                        TestRead()
                    }
                }
            }
        }
    }

    @Composable
    private fun TestRead() {
        Log.d("Obd2ReadingActivity", deviceAddress)
        val service = Obd2Service(deviceAddress)
        var rpm by remember { mutableStateOf("0") }
        var buttonClicked by remember { mutableStateOf(false) }

        Button(onClick = { buttonClicked = true }) {
            Text("Get RPM")
        }

        LaunchedEffect(buttonClicked) {
            if (buttonClicked) {
                rpm = getRpm(service)
                buttonClicked = false // Reset the buttonClicked state
            }
        }

        Text(text = "CurrentRpm $rpm")
    }

    private suspend fun getRpm(service: Obd2Service): String {
        // Your suspend logic here
        // For example, you can call a suspend function from your service
        return withContext(Dispatchers.IO) {
            // Call your suspend function here
            // For example:
            // service.getRpm()
            service.getEltonApiRPM()
        }
    }
}