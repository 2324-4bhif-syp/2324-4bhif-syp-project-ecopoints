package at.htl.ecopoints.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.service.Obd2Service
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import kotlinx.coroutines.launch
import java.util.UUID


class Obd2ReadingActivity : ComponentActivity() {
    var deviceName = ""
    var deviceAddress = ""

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
        Log.d("Obd2ReadingActivity", "$deviceAddress")
        val service = Obd2Service(deviceAddress)
        val coroutineScope = rememberCoroutineScope()
        var rpm = "nix"

        val getRPMOnClick: () -> Unit = {
            coroutineScope.launch {
                rpm = service.getRpm()
            }
        }

        Button(onClick = getRPMOnClick) {
            Text("Get RPM")
        }

        Text(text = "CurrentRpm ${rpm}")
    }
}