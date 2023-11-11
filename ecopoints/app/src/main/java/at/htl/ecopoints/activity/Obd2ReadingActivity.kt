package at.htl.ecopoints.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.service.Obd2Sercvice
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.command.temperature.EngineCoolantTemperatureCommand
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import kotlinx.coroutines.launch
import java.util.UUID


class Obd2ReadingActivity : ComponentActivity() {
    var deviceName = ""
    var deviceUUID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    deviceName = intent.getStringExtra("deviceName") ?: ""
    deviceUUID = intent.getStringExtra("deviceName") ?: ""

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
                        Text(text = "Address: $deviceUUID")
                    }
                }
            }
        }
    }



    @Composable
    private fun testRead() {
        val service = Obd2Sercvice(UUID.fromString(deviceUUID))
        val coroutineScope = rememberCoroutineScope()
        var rpm = "nix"

        val getLocationOnClick: () -> Unit = {
            coroutineScope.launch {
                rpm = service.getRpm()
            }
        }

        Text(text = "CurrentRpm ${rpm}")
    }
}