package at.htl.ecopoints.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.android.animation.SegmentType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htl.ecopoints.service.BluetoothDeviceListService
import at.htl.ecopoints.service.BluetoothService
import at.htl.ecopoints.service.Obd2Service
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TripActivity : ComponentActivity() {
    private var selectedDevice: BluetoothDevice? = null
    private val bluetoothDeviceService = BluetoothDeviceListService()
    private val bluetoothService: BluetoothService = BluetoothService()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var showDialog: Boolean by remember { mutableStateOf(false) }
            var deviceNameText by remember { mutableStateOf("Not Selected") }
            var isConnecting by remember { mutableStateOf(false) }
            var connection by remember { mutableStateOf(false) }


            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    if (isConnecting) {

                        ConnectToDevice(
                            connecting = isConnecting,
                            selectedDevice,
                            onDismiss = { isConnecting = false },
                            onConnect = { it ->
                                isConnecting = it
                                connection = it
                            })
                    }

                    Text(
                        text = "Trip",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 24.sp
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    TestReadCustomComm()
                        Spacer(modifier = Modifier.height(16.dp)) // Space between text and buttons

                        StartStopButton()
                        ConnectionInfo(deviceNameText, connection)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { isConnecting = true },
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

    @Composable
    private fun TestReadCustomComm() {
        if (selectedDevice != null) {

            var rpm by remember { mutableStateOf("0") }
            var speed by remember { mutableStateOf("0") }
            var coolantTemp by remember { mutableStateOf("0") }
            var buttonClicked by remember { mutableStateOf(false) }

            val service = Obd2Service(selectedDevice!!.address)


            Button(onClick = { buttonClicked = true }) {
                Text("Read with Custom Comm")
            }

            LaunchedEffect(buttonClicked) {
                if (buttonClicked) {
                    service.initOBD()
                    for (i in 0..1000) {

                        var rpm1 = service.getRPM()
//                    delay(500)
                        //var speed2 = service.getSpeed()
//                    var coolantTemp3 = service.getCoolantTemp()

                        if (rpm1 != "0") {
                            rpm = rpm1
                        }
//                    if (speed2 != "0") {
//                        speed = speed2
//                    }
//                    if (coolantTemp3 != "0") {
//                        coolantTemp = coolantTemp3
//                    }
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

    private fun onStartBtnClick() {

    }

    private fun onStopBtnClick() {

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
    fun ConnectionInfo(deviceName: String, connection: Boolean = false) {
        var connectionState = "Not Connected"
        var connectionStateColor: Color = Color.Red
        if (connection) {
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

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun ConnectToDevice(
        connecting: Boolean,
        device: BluetoothDevice?,
        onDismiss: () -> Unit,
        onConnect: (Boolean) -> Unit
    ) {
        if (device == null) {
            NoDeviceSelectedAlert(onDismiss = onDismiss)
        } else if (connecting) {

            CoroutineScope(Dispatchers.Main).launch {
                bluetoothService.connectDevice(device)
                onConnect(bluetoothService.connected())
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
    }

    @Composable
    fun NoDeviceSelectedAlert(onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "No Device Selected") },
            text = { Text(text = "Please select a device to connect.") },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("OK")
                }
            }
        )
    }
}