package at.htl.ecopoints.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import at.htl.ecopoints.db.DBHelper
import at.htl.ecopoints.interfaces.OnLocationChangedListener
import at.htl.ecopoints.model.Car
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.service.BluetoothDeviceListService
import at.htl.ecopoints.service.TestLocationService
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.github.eltonvs.obd.command.ObdCommand
import com.github.eltonvs.obd.command.ObdResponse
import com.github.eltonvs.obd.connection.ObdDeviceConnection
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
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class TripActivity : ComponentActivity(), OnLocationChangedListener {
    private val tag = "TripActivity"
    private var selectedDevice: BluetoothDevice? = null
    private val bluetoothDeviceService = BluetoothDeviceListService()
    private val testLocationService: TestLocationService by lazy {
        TestLocationService(this)
    }
    private var longitude = 14.285830
    private var latitude = 48.306940
    private var latLngHasChanged = mutableStateOf(false)
    private var tripActive = false
    private var carDataList = mutableStateListOf<CarData>()
    private val latLngList = mutableStateListOf<Pair<Color, Pair<LatLng, Double>>>()


    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "SourceLockedOrientationActivity"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Trip") }

            var showDialog: Boolean by remember { mutableStateOf(false) }
            var showSelectCarDialog: Boolean by remember { mutableStateOf(false) }
            var showBigMap: Boolean by remember { mutableStateOf(false) }
            var deviceNameText by remember { mutableStateOf("Not Selected") }
            var isConnecting by remember { mutableStateOf(false) }
            var connection by remember { mutableStateOf("Not Connected") }
            val currentLocation = LatLng(latitude, longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
            }
            var mapProperties by remember {
                mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))
            }
            val activity = LocalContext.current as Activity
            var selectedCar: Car? = null
            var showSelectedCarInformation: Boolean by remember { mutableStateOf(false) }
            testLocationService.setOnLocationChangedListener(this)


            EcoPointsTheme {
                activity.requestedOrientation =
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(backgroundColor = MaterialTheme.colorScheme.background, topBar = {
                        TopAppBar(backgroundColor = MaterialTheme.colorScheme.background, title = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Button(onClick = { showSelectCarDialog = true }) {
                                    Text(text = "Select your car")

                                }

//                                MapTypeControls(onMapTypeClick = {
//                                    Log.d("GoogleMap", "Selected map type $it")
//                                    mapProperties = mapProperties.copy(mapType = it)
//                                })
                            }
                        })
                    }) {

                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@TripActivity,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                1
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Button(
                                    onClick = { showBigMap = true },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.background(Color.Green)
                                ) {
                                    GoogleMap(
                                        modifier = Modifier
                                            .height(150.dp)
                                            .width(150.dp),
                                        cameraPositionState = cameraPositionState,
                                        properties = mapProperties,
                                    ) {
                                        DrawPolyline()
                                    }
                                }
                            }
                        }
                    }


                    //TESTING
//                    ReadTest()

                    Column (verticalArrangement = Arrangement.Center){
                        if (showSelectedCarInformation) {
                            ShowCurrentCar(car = selectedCar!!)
                        }

                    }

                    if (showSelectCarDialog) {
                        SelectCarDialog(
                            showDialog = showSelectCarDialog,
                            onDismiss = { showSelectCarDialog = false },
                            onCarSelected = { car ->
                                selectedCar = car
                                showSelectedCarInformation = true;
                            }
                        )
                    }

                    if (showBigMap) {
                        Dialog(onDismissRequest = { /*TODO*/ }) {
                            Column(Modifier.background(MaterialTheme.colorScheme.background)) {
                                GoogleMap(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(200.dp),
                                    properties = mapProperties,
                                ) {
                                    DrawPolyline()
                                }
                            }
                        }
                    }

                    if (isConnecting) {
                        Connect(
                            selectedDevice,
                            onDismiss = { isConnecting = false },
                            onConnect = {
                                connection = it
                            })
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        StartStopButton()
                        ConnectionInfo(deviceNameText, connection)

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                shape = MaterialTheme.shapes.medium,
                                onClick = { isConnecting = true },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                            ) {
                                Text(text = "Connect")
                            }
                            Button(
                                shape = MaterialTheme.shapes.medium,
                                onClick = { showDialog = true },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(150.dp)
                            ) {
                                Text(text = "Select Device")
                            }

                        }
                        Spacer(modifier = Modifier.height(50.dp))


                        if (ActivityCompat.checkSelfPermission(
                                this@TripActivity, Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@TripActivity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1
                            )
                        } else {
                            BluetoothDeviceSelectionDialog(pairedDevices = bluetoothDeviceService.getAllDevices(),
                                showDialog = showDialog,
                                onDismiss = { showDialog = false },
                                onDeviceSelected = { device ->
                                    selectedDevice = device
                                    showDialog = false
                                    deviceNameText = device.name
                                })
                        }
                    }


                    Box {
                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@TripActivity
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SelectCarDialog(
        showDialog: Boolean,
        onDismiss: () -> Unit,
        onCarSelected: (Car) -> Unit
    ) {
        var model by remember { mutableStateOf(TextFieldValue()) }
        var manufacturer by remember { mutableStateOf(TextFieldValue()) }
        var year by remember { mutableStateOf("0") }
        var fuelType by remember { mutableStateOf("nothing") }
        val years = listOf("2024", "2023", "2022", "2021", "2020") // Example years
        val fuelTypes = listOf("Gasoline", "Diesel", "Electric", "Hybrid") // Example fuel types

        if (showDialog) {
            AlertDialog(onDismissRequest = onDismiss,
                title = { Text("Select your Car") },
                text = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        TextField(
                            value = model,
                            onValueChange = { model = it },
                            label = { Text("Model") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = manufacturer,
                            onValueChange = { manufacturer = it },
                            label = { Text("Manufacturer") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownMenu( //Todo
                            expanded = false,
                            onDismissRequest = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            years.forEach { y ->
                                DropdownMenuItem(onClick = {
                                    year = y
                                }) {
                                    Text(y)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            fuelTypes.forEach { ft ->
                                DropdownMenuItem(onClick = {
                                    fuelType = ft
                                }) {
                                    Text(ft)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val car = Car(
                            id = 0,
                            model = model.text,
                            brand = manufacturer.text,
                            year = year,
                            fuelType = fuelType
                        )
                        onCarSelected(car)
                        onDismiss()
                    }) {
                        Text("Select")
                    }
                })
        }
    }


    @Composable
    private fun ShowCurrentCar(car: Car?) {
        Log.d(tag, "Car: $car")
        if (car != null) {
            Log.d(tag, "Car: $car")
            Column {
                Text(text = "Model: ${car.model}")
                Text(text = "Manufacturer: ${car.brand}")
                Text(text = "Year: ${car.year}")
                Text(text = "Fuel Type: ${car.fuelType}")

            }
        }
    }

    private fun onStartBtnClick() {
        tripActive = true
        carDataList.clear()
        Log.d("TripActivity", "Trip started")
    }

    private fun onStopBtnClick() {
        tripActive = false

        lifecycleScope.launch(Dispatchers.IO)
        {
            val db = DBHelper(this@TripActivity, null)

            carDataList.forEach {
                Log.d(tag, it.toString())
                db.addCarData(it)
            }
            db.syncCarDataWithBackend()
        }
        Log.d("TripActivity", "Trip stopped")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun Connect(
        device: BluetoothDevice?,
        onDismiss: () -> Unit,
        onConnect: (String) -> Unit
    ) {
        if (device == null) {
            NoDeviceSelectedAlert(onDismiss = onDismiss)
        } else {
            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

            var inputStream: InputStream? by remember { mutableStateOf(null) }
            var outputStream: OutputStream? by remember { mutableStateOf(null) }
            var bluetoothSocket: BluetoothSocket? by remember { mutableStateOf(null) }
            var isConnected by remember {
                mutableStateOf(false)
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@TripActivity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1
                )
            } else {

                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            onConnect("Connecting ...")
                            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                            bluetoothSocket?.connect()
                            inputStream = bluetoothSocket?.inputStream
                            outputStream = bluetoothSocket?.outputStream
                            onConnect("Connected")
                            Log.d(tag, inputStream.toString())
                            Log.d(tag, outputStream.toString())
                            isConnected = true
                        } catch (e: IOException) {
                            Log.e(tag, e.toString())
                            onConnect("Failed to connect")
                        }
                    }
                }
            }

            if (isConnected) {
                Read(
                    bluetoothSocket = bluetoothSocket,
                    inputStream = inputStream,
                    outputStream = outputStream
                )
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun Read(
        bluetoothSocket: BluetoothSocket?, inputStream: InputStream?, outputStream: OutputStream?
    ) {
        var rpm by remember { mutableStateOf("3") }
        var speed by remember { mutableStateOf("0") }
        var vin by remember { mutableStateOf("0") }

        if (bluetoothSocket != null && inputStream != null && outputStream != null) {
//            LaunchedEffect(Unit) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        withContext(Dispatchers.IO) {
//                            while (vin.length < 10) {
//                                try {
//                                    val vincommand = VinCommand()
//                                    vincommand.run(
//                                        bluetoothSocket.getInputStream(),
//                                        bluetoothSocket.getOutputStream()
//                                    )
//
//                                    vin = vincommand.formattedResult
//                                } catch (e: Exception) {
//                                    Log.d(tag, e.toString())
//                                }
//                            }
//                        }
//
//                    } catch (e: Exception) {
//                        Log.e(tag, e.toString())
//                    }
//                }
//            }
//
//            if (vin.length > 10)
            LaunchedEffect(Unit)
            {
                while (true) {
                    lifecycleScope.launch {

                        val res = fetchDataAsync(
                            command = com.github.eltonvs.obd.command.engine.RPMCommand(),
                            inputStream = inputStream,
                            outputStream = outputStream
                        )

                        if (res != null) {

                            Log.d(tag, res.rawResponse.value);
                            val e = res.rawResponse.value.split(" ")

                            if (e.size >= 5) {
                                val combinedHex = e[3] + e[4]

                                val combinedDecimal = combinedHex.toInt(16)
                                rpm = (combinedDecimal / 4).toString()

                            }
                        }

//                        speed = fetchDataAsync(
//                            command = SpeedCommand(),
//                            inputStream = inputStream,
//                            outputStream = outputStream
//                        )
                        Log.d(tag, "RPM: $rpm")
                        Log.d(tag, "Speed: $speed")
                    }

                    // Add a delay to avoid running the loop too frequently
                    delay(500)
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            )
            {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Current-Speed ${speed}")
                Text(text = "Current-Rpm ${rpm}")
                Text(text = "VIN ${vin}")
            }
        }
    }

    private suspend fun fetchDataAsync(
        command: ObdCommand,
        inputStream: InputStream,
        outputStream: OutputStream
    ): ObdResponse? = withContext(Dispatchers.IO) {
        try {
            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            obdConnection.run(command)
        } catch (e: Exception) {
            Log.e(tag, e.toString())
            // Return a default response or handle errors as needed
            null
        }
    }

    @Composable
    fun StartStopButton() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                shape = MaterialTheme.shapes.medium,

                onClick = { onStartBtnClick() }, modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = "Start")
            }

            Button(
                shape = MaterialTheme.shapes.medium,

                onClick = { onStopBtnClick() }, modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = "Stop")
            }
        }
    }

    @Composable
    fun ConnectionInfo(deviceName: String, connection: String) {
        var connectionStateColor: Color = Color.Red
        if (connection == "Connected") connectionStateColor = Color.Green
        else if (connection == "Connecting ...") connectionStateColor =
            Color.Yellow


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
                    text = connection,
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

    @Composable
    fun BluetoothDeviceSelectionDialog(
        pairedDevices: List<BluetoothDevice>,
        showDialog: Boolean,
        onDismiss: () -> Unit,
        onDeviceSelected: (BluetoothDevice) -> Unit
    ) {
        if (showDialog) {
            AlertDialog(onDismissRequest = onDismiss,
                title = { Text("Select a Bluetooth Device") },
                text = {
                    LazyColumn {
                        items(
                            pairedDevices.filter {
                                if (ActivityCompat.checkSelfPermission(
                                        this@TripActivity,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    ActivityCompat.requestPermissions(
                                        this@TripActivity,
                                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1
                                    )
                                }
                                it.name.lowercase().contains("obd")
                            }
                        ) { device ->
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
                })
        }
    }

    @Composable
    fun NoDeviceSelectedAlert(onDismiss: () -> Unit) {
        AlertDialog(onDismissRequest = { onDismiss() },
            title = { Text(text = "No Device Selected") },
            text = { Text(text = "Please select a device to connect.") },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("OK")
                }
            })
    }

// region Map


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
    private fun MapButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Button(
            modifier = modifier.padding(4.dp), onClick = onClick
        ) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }

    @Composable
    private fun DrawPolyline() {
        if (!latLngHasChanged.value) {
            for (i in 0 until latLngList.size - 1) {
                Polyline(
                    points = listOf(
                        latLngList[i].second.first,
                        latLngList[i + 1].second.first
                    ),
                    color = latLngList[i].first,
                    width = 10f
                )
            }
        }
        latLngHasChanged.value = false
    }

    private fun addItemToList(newItem: LatLng) {
        val fuelCons = generateRandomFuelCons()
        val color = when {
            fuelCons <= 6.0 -> Color.Green //Green
            fuelCons > 6.0 && fuelCons <= 12 -> Color.Green  //Yellow
            fuelCons > 12 && fuelCons <= 20 -> Color.Green //Red
            else -> Color.Green //Black
        }
        latLngList.add(Pair(color, Pair(newItem, fuelCons)))
        latLngHasChanged.value = true

        Log.d("FuelTracking", "Fuel consumption: $fuelCons")
    }

    //for testing purposes, remove if database is set up
    private fun generateRandomFuelCons(): Double {
        return (3..21).random().toDouble()
    }

    private fun haversine(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(
                Math.toRadians(lat2)
            ) * sin(
                dLon / 2
            ) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c * 1000
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

        if (isLocationChanged(
                newLatitude,
                newLongitude,
                latitude,
                longitude,
                1.0
            )
        ) {
            latitude = newLatitude
            longitude = newLongitude

            Log.d(
                "MapTracking",
                "Location changed to $latitude, $longitude"
            )
            addItemToList(LatLng(latitude, longitude))
        }
    }

//endregion
}