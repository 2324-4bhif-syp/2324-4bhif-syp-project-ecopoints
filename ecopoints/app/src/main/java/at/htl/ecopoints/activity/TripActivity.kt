package at.htl.ecopoints.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htl.ecopoints.service.BluetoothDeviceListService
import at.htl.ecopoints.ui.theme.EcoPointsTheme

class TripActivity : ComponentActivity() {
    private var selectedDevice: BluetoothDevice? = null
    private val bluetoothDeviceService = BluetoothDeviceListService()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var showDialog: Boolean by remember { mutableStateOf(false) }
            var deviceNameText by remember { mutableStateOf("Not Selected") }
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp)) // Space between text and buttons

                        StartStopButton()
                        ConnectionInfo(deviceNameText)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { onConnectBtnClick() },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                            ) {
                                Text(text = "Connect")
                            }
                            Button(
                                onClick = { showDialog = true },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(150.dp)
                            ) {
                                Text(text = "Select Device")
                            }
                        }
                        BluetoothDeviceSelectionDialog(
                            pairedDevices = bluetoothDeviceService.getAllDevices(),
                            showDialog = showDialog,
                            onDismiss = { showDialog = false },
                            onDeviceSelected = { device ->
                                selectedDevice = device
                                showDialog = false
                                deviceNameText = device.name
                            }
                        )
                    }
                }
            }
        }
    }

    private fun onStartBtnClick() {

    }

    private fun onStopBtnClick() {

    }

    private fun onConnectBtnClick() {

    }

    @Composable
    fun StartStopButton() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onStartBtnClick() },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = "Start")
            }

            Button(
                onClick = { onStopBtnClick() },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = "Stop")
            }
        }
    }

    @Composable
    fun ConnectionInfo(deviceName : String, connection : Boolean = false) {
        var connectionState = "Not Connected"
        var connectionStateColor : Color = Color.Red
        if(connection){
            connectionState = "Connected"
            connectionStateColor = Color.Green
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Connection:",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                )

                Text(
                    text = "Device:",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                )
            }

            Column(
                modifier = Modifier.weight(0.4f)
            ) {
                Text(
                    text = connectionState,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                    color = connectionStateColor
                )

                Text(
                    text = deviceName,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    fun BluetoothDeviceSelectionDialog(
        pairedDevices: List<BluetoothDevice>,
        showDialog: Boolean,
        onDismiss: () -> Unit,
        onDeviceSelected: (BluetoothDevice) -> Unit
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("Select a Bluetooth Device") },
                text = {
                    LazyColumn {
                        items(pairedDevices) { device ->
                            TextButton(
                                onClick = { onDeviceSelected(device) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(device.name)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}