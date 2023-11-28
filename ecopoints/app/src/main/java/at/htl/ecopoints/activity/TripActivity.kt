package at.htl.ecopoints.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import at.htl.ecopoints.service.BluetoothDeviceListService
import at.htl.ecopoints.service.BluetoothService
import at.htl.ecopoints.service.Obd2Service
import at.htl.ecopoints.service.TestLocationService
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.interfaces.OnLocationChangedListener
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TripActivity : ComponentActivity(), OnLocationChangedListener {
    private var selectedDevice: BluetoothDevice? = null
    private val bluetoothDeviceService = BluetoothDeviceListService()
    private val bluetoothService: BluetoothService = BluetoothService()
    private val testLocationService: TestLocationService by lazy {
        TestLocationService(this)
    }
    private var longitude = 14.285830
    private var latitude = 48.306940
    private var latLngHasChanged = mutableStateOf(false)
    private var tripActive = false

    private val latLngList =
        mutableStateListOf<Pair<Color, Pair<LatLng, Double>>>()


    @SuppressLint("MissingPermission", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showDialog: Boolean by remember { mutableStateOf(false) }
            var deviceNameText by remember { mutableStateOf("Not Selected") }
            var isConnecting by remember { mutableStateOf(false) }
            var connection by remember { mutableStateOf(false) }
            val currentLocation = LatLng(latitude, longitude)
            var cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
            }
            var mapProperties by remember {
                mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))
            }
            val activity = LocalContext.current as Activity

            testLocationService.setOnLocationChangedListener(this)

            EcoPointsTheme {
                activity.requestedOrientation =
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        backgroundColor = MaterialTheme.colorScheme.background,
                        topBar = {
                            TopAppBar(
                                backgroundColor = MaterialTheme.colorScheme.background,
                                title = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    MapTypeControls(onMapTypeClick = {
                                        Log.d("GoogleMap", "Selected map type $it")
                                        mapProperties = mapProperties.copy(mapType = it)
                                    })
                                }
                            })
                        }) {
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            cameraPositionState = cameraPositionState,
                            properties = mapProperties,
                        ) {
                            DrawPolyline()
                        }
                    }

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

                        if (ActivityCompat.checkSelfPermission(
                                this@TripActivity,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@TripActivity,
                                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                                1
                            )
                        } else {
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
    }

    private fun onStartBtnClick() {
        tripActive = true
        Log.d("TripActivity", "Trip started")
    }

    private fun onStopBtnClick() {
        tripActive = false
        // save the trip to the database
        //TODO: save the trip to the database

        Log.d("TripActivity", "Trip stopped")
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
                        var speed2 = service.getSpeed()
                        var coolantTemp3 = service.getCoolantTemp()

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

    @Composable
    private fun DrawPolyline() {
        if (!latLngHasChanged.value) {
            for (i in 0 until latLngList.size - 1) {
                Polyline(
                    points = listOf(latLngList[i].second.first, latLngList[i + 1].second.first),
                    color = latLngList[i].first,
                    width = 10f
                )
            }
        }
        latLngHasChanged.value = false
    }

    @Composable
    private fun MapTypeControls(
        onMapTypeClick: (MapType) -> Unit
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(state = ScrollState(0)),
            horizontalArrangement = Arrangement.Center
        ) {
            MapType.values().forEach {
                MapTypeButton(type = it) { onMapTypeClick(it) }
            }
        }
    }

    @Composable
    private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
        MapButton(text = type.toString(), onClick = onClick)

    @Composable
    private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
        Button(
            modifier = modifier.padding(4.dp),
            onClick = onClick
        ) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }

    private fun addItemToList(newItem: LatLng) {
        val fuelCons = generateRandomFuelCons()
        val color = when {
            fuelCons <= 6.0 -> Color.Green
            fuelCons > 6.0 && fuelCons <= 12 -> Color.Yellow
            fuelCons > 12 && fuelCons <= 20 -> Color.Red
            else -> Color.Black
        }
        latLngList.add(Pair(color, Pair(newItem, fuelCons)))
        latLngHasChanged.value = true

        Log.d("FuelTracking", "Fuel consumption: $fuelCons")
    }

    //for testing purposes, remove if database is set up
    private fun generateRandomFuelCons(): Double {
        return (3..21).random().toDouble()
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c * 1000
    }

    private fun isLocationChanged(
        newLat: Double,
        newLon: Double,
        oldLat: Double,
        oldLon: Double,
        threshold: Double
    ): Boolean {
        val distance = haversine(newLat, newLon, oldLat, oldLon)
        return distance > threshold
    }

    override fun onLocationChanged(lat: String, lon: String) {
        val newLatitude = lat.toDouble()
        val newLongitude = lon.toDouble()

        if (isLocationChanged(newLatitude, newLongitude, latitude, longitude, 1.0)) {
            latitude = newLatitude
            longitude = newLongitude

            Log.d("MapTracking", "Location changed to $latitude, $longitude")
            addItemToList(LatLng(latitude, longitude))
        }
    }
}