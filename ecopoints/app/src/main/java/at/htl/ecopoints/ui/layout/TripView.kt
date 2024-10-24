package at.htl.ecopoints.ui.layout

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import at.htl.ecopoints.io.*
import at.htl.ecopoints.io.LocationManager
import at.htl.ecopoints.model.*
import at.htl.ecopoints.model.viewmodel.TripViewModel
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.component.ShowMap
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

private val TAG = TripView::class.java.simpleName

@Singleton
class TripView {

    @Inject
    lateinit var store: Store

//    @Inject
//    lateinit var obdReader: ObdReader

    @Inject
    lateinit var obdReaderKt: ObdReaderKt

    @Inject
    lateinit var btConnectionHandler: BtConnectionHandler

    @Inject
    lateinit var speedCalculator: GpsSpeedCalculator

    @Inject
    lateinit var writer: JsonFileWriter

    private var tripActive = false

    @Inject
    constructor()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint(
        "CheckResult", "UnusedMaterial3ScaffoldPaddingParameter",
        "UnrememberedMutableState"
    )
    fun compose(activity: ComponentActivity) {
        activity.setContent {

            LocationManager(activity.applicationContext) { location ->

                val loc: Location = Location(location.latitude, location.longitude, Date())
//                Log.i(TAG, "SPEED: " + speedCalculator.calculateSpeed(loc).toString() + " km/h")

                store.next {
                    it.tripViewModel.carData["Latitude"] = location.latitude.toString()
                    it.tripViewModel.carData["Longitude"] = location.longitude.toString()
                    it.tripViewModel.carData["Altitude"] = location.altitude.toString()
                    it.tripViewModel.carData["Gps-Speed"] = ((location.speed * 10.0 * 3.6).roundToInt() / 10).toString()
                    it.tripViewModel.carData["Armin-Speed"] =  ((speedCalculator.calculateSpeed(loc)*10.0).roundToInt() / 10).toString()
//                        val fuelCons = generateRandomFuelCons()
//                        it.tripViewModel.map.add(
//                            location.latitude, location.longitude,
//                            fuelCons
//                        )
                }
            }

            val isDarkMode = store.subject.map { it.isDarkMode }.subscribeAsState(false)

            EcoPointsTheme(
                darkTheme = isDarkMode.value
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 4.dp,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    /*Button(
                                        onClick = {
                                            store.next { it.tripViewModel.map.showMap = true }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        shape = MaterialTheme.shapes.medium,
                                        elevation = ButtonDefaults.buttonElevation(4.dp)
                                    ) {
                                        Text(text = "Map", style = MaterialTheme.typography.bodyLarge)
                                    }*/

                                    Button(
                                        onClick = {
                                            store.next { it.tripViewModel.showTestCommandDialog = true }
                                        },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        ),
                                        shape = MaterialTheme.shapes.medium,
                                        elevation = ButtonDefaults.buttonElevation(4.dp)
                                    ) {
                                        Text(text = "Test Commands", style = MaterialTheme.typography.bodyLarge)
                                    }

                                    IconButton(
                                        onClick = {
                                            store.next { it.isDarkMode = !it.isDarkMode }
                                        },
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Brightness4,
                                            contentDescription = "Change Theme",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            Column(modifier = Modifier.padding(bottom = 50.dp)) {
                                ConnectionInfo(store, btConnectionHandler)
                            }

                            Column {
                                val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Trip") }
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    BottomNavBar(
                                        currentScreen = currentScreen,
                                        onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                                        context = activity
                                    )
                                }
                            }
                        }
                    ) {
                        // Content that can scroll if necessary
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())  // Makes the content scrollable
                                .padding(it)
                        ) {
                            // Remove large spacer for now to test layout
                            Spacer(modifier = Modifier.height(16.dp))  // Small dynamic spacer

                            // Ensure that LiveCarData is inside a container that allows it to render
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)  // Add some padding for visibility
                            ) {
                                LiveCarData(store, context = activity.applicationContext)  // Display your car data here
                            }

                            // Other composables below
                            BtDeviceSelectionDialog(store, btConnectionHandler)
                            ShowMapCard(store = store)
                            ShowTestObdCommandsCard(store = store)
                        }
                    }
                }
            }
        }
    }

    private fun stopTrip() {
        if (tripActive) {
            Log.i(TAG, "Trip stopped")
            obdReaderKt.stopReading()
            tripActive = false
        } else {
            Log.w(TAG, "Tried to stop a trip without starting one")
        }
    }

    private fun startTrip() {
        //View Testing
//        store.next{it.tripViewModel.isConnected = true}

        if (store.subject.value?.tripViewModel?.isConnected == true) {
            Log.i(TAG, "Trip started")
            obdReaderKt.startReading(
                btConnectionHandler.inputStream,
                btConnectionHandler.outputStream
            )
            tripActive = true
        } else {
            Log.w(TAG, "Tried to start a trip without a connection to a device")
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowAlert(
        title: String = "Alert",
        text: String = "Alert",
        onDismiss: () -> Unit, // Callback when the dialog is dismissed
    ) {
        AlertDialog(
            onDismissRequest = onDismiss, // Called when the user tries to dismiss the dialog
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            ),
            title = { Text(title) }, // Title of the dialog (optional)
            text = { Text(text) }, // Main content of the dialog
            confirmButton = {
                Button(
                    onClick = onDismiss // Call the onDismiss callback when the button is clicked
                ) {
                    Text("Close")
                }
            }
        )
    }

//region LiveData Visual

    @Composable
    fun LiveCarData(store: Store, context : Context) {
        val isConnectedState =
            store.subject.map { it.tripViewModel.isConnected }.subscribeAsState(false)
        val state = store.subject.map { it.tripViewModel.carData }
            .subscribeAsState(ConcurrentHashMap<String, String>())

        LaunchedEffect(key1 = isConnectedState) {
            store.next { store ->
                obdReaderKt.carDataCommands.forEach {
                    store.tripViewModel.carData[it.name] = "0"
                }
            }
        }

        // Main Column for the dashboard layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val data = state.value

            SectionHeader(
                title = "Vehicle Data",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp)  // Reduced padding for less top space
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                InfoCard(
                    title = "GPS-Speed",
                    value = "${data["Gps-Speed"] ?: "0"} km/h",
                    icon = Icons.Default.LocationOn
                )

                InfoCard(
                    title = "Obd-Speed",
                    value = "${data["Vehicle Speed"] ?: "0"} km/h",
                    icon = Icons.Default.Speed
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                InfoCard(
                    title = "RPM",
                    value = "${data["Engine RPM"] ?: "0"} rpm",
                    icon = Icons.Default.RotateRight
                )

                InfoCard(
                    title = "Fuel Pressure",
                    value = "${data["Fuel Rail Gauge Pressure"] ?: "0"} bar",
                    icon = Icons.Default.LocalGasStation
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                InfoCard(
                    title = "Engine Load",
                    value = "${data["Engine Load"] ?: "0"}%",
                    icon = Icons.Default.Build
                )

                InfoCard(
                    title = "Throttle Position",
                    value = "${data["Throttle Position"] ?: "0"}%",
                    icon = Icons.Default.Tune
                )
            }


            // Row for Distance, Driving Time, and Avg Speed
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(
                    title = "Coolant Temp",
                    value = "${data["Engine Coolant Temperature"] ?: "0"} °C",
                    icon = Icons.Default.Thermostat
                )
                InfoCard(
                    title = "Air intake Temp",
                    value = "${data["Ambient Air Temperature"] ?: "0"} °C",
                    icon = Icons.Default.Air
                )
            }


            SectionHeader(
                title = "GPS Data",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard2(
                    title = "Latitude",
                    value = "${(data["Latitude"]?.toFloatOrNull() ?: 0f).let { String.format("%.1f", it) }}",
                    icon = Icons.Default.LocationOn,
                    modifier = Modifier.weight(1f) // Ensure equal width
                )

                InfoCard2(
                    title = "Longitude",
                    value = "${(data["Longitude"]?.toFloatOrNull() ?: 0f).let { String.format("%.1f", it) }}",
                    icon = Icons.Default.LocationOn,
                    modifier = Modifier.weight(1f) // Ensure equal width
                )

                InfoCard2(
                    title = "Altitude",
                    value = "${(data["Altitude"]?.toFloatOrNull() ?: 0f).let { String.format("%.1f", it) }} m",
                    icon = Icons.Default.Terrain,
                    modifier = Modifier.weight(1f) // Ensure equal width
                )
            }


            GForceMonitor(context)
        }
    }


    @Composable
    fun InfoCard(title: String, value: String, icon: ImageVector) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(120.dp)
                .height(100.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun InfoCard2(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier
                .padding(8.dp)
                .height(120.dp),  // Adjust height
            elevation = CardDefaults.cardElevation(6.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,  // Limit to 1 line
                        overflow = TextOverflow.Ellipsis,  // Ellipsis if text is too long
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 1,  // Limit to 1 line for the value
                        overflow = TextOverflow.Ellipsis // Ellipsis if text is too long
                    )
                }
            }
        }
    }



    @Composable
    fun SectionHeader(title: String, modifier: Modifier = Modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .padding(vertical = 5.dp, horizontal = 5.dp),
            textAlign = TextAlign.Center
        )
    }



    // Components for the custom UI elements with size adjustments
    @Composable
    fun Gauge(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier
//                .padding(start = 20.dp, end = 20.dp, top = 3.dp, bottom = 3.dp)
                .height(70.dp)
                .width(110.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "$value $unit",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }



    @Composable
    fun HorizontalBar(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(4.dp)
                .size(100.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall, fontSize = 12.sp)
            // Simulate a horizontal bar here (you can use a ProgressBar or a custom Canvas)
            Text(
                text = "$value $unit",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
        }
    }



    @Composable
    fun GForceMonitor(context: Context) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val xForce = remember { mutableStateOf(0f) }
        val yForce = remember { mutableStateOf(0f) }
        val zForce = remember { mutableStateOf(0f) }
        val totalGForce = remember { mutableStateOf(0f) }

        val xOffset = remember { mutableStateOf(0f) }
        val yOffset = remember { mutableStateOf(0f) }
        val zOffset = remember { mutableStateOf(0f) }

        val isCalibrated = remember { mutableStateOf(false) }

        val gravity = FloatArray(3)

        val previousGForces = remember { mutableListOf<Float>() }
        val smoothingFactor = 0.1f

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val alpha = 0.8f

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

                val linearAccelerationX = event.values[0] - gravity[0]
                val linearAccelerationY = event.values[1] - gravity[1]
                val linearAccelerationZ = event.values[2] - gravity[2]

                val calibratedX =
                    if (isCalibrated.value) linearAccelerationX - xOffset.value else linearAccelerationX
                val calibratedY =
                    if (isCalibrated.value) linearAccelerationY - yOffset.value else linearAccelerationY
                val calibratedZ =
                    if (isCalibrated.value) linearAccelerationZ - zOffset.value else linearAccelerationZ

                xForce.value = calibratedX
                yForce.value = calibratedY
                zForce.value = calibratedZ

                val gX = calibratedX / SensorManager.GRAVITY_EARTH
                val gY = calibratedY / SensorManager.GRAVITY_EARTH
                val gZ = calibratedZ / SensorManager.GRAVITY_EARTH

                val newTotalGForce = kotlin.math.sqrt(gX * gX + gY * gY + gZ * gZ)

                if (previousGForces.size > 0) {
                    totalGForce.value =
                        (previousGForces.last() * (1 - smoothingFactor)) + (newTotalGForce * smoothingFactor)
                } else {
                    totalGForce.value = newTotalGForce
                }

                previousGForces.add(totalGForce.value)

                if (previousGForces.size > 100) {
                    previousGForces.removeAt(0)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        DisposableEffect(Unit) {
            sensorManager.registerListener(
                sensorEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI
            )
            onDispose {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display total G-Force value
            Text(
                text = "Total G-Force: ${totalGForce.value} g",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Display G-Force visualization
            GForceDisplay(
                xForce = xForce.value,
                yForce = yForce.value,
                zForce = zForce.value,
                modifier = Modifier.padding(16.dp)
            )

            // Calibration button
            Button(
                onClick = {
                    // Set the current g-forces as the offsets (calibration)
                    xOffset.value = xForce.value
                    yOffset.value = yForce.value
                    zOffset.value = zForce.value
                    isCalibrated.value = true
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Calibrate")
            }
        }
    }

        @Composable
        fun GForceDisplay(xForce: Float, yForce: Float, zForce: Float, modifier: Modifier = Modifier) {
            val maxForce = 10f  // Arbitrary max force for scaling (adjust as needed)

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(300.dp)) {
                    val canvasSize = size.minDimension
                    val radius = canvasSize / 2
                    val center = Offset(radius, radius)

                    // Draw outer circle
                    drawCircle(color = Color.LightGray, radius = radius)

                    // Map the g-forces to the screen space (adjust scaling as needed)
                    val dotX = (xForce / maxForce) * radius
                    val dotY = (yForce / maxForce) * radius

                    // Draw the dot representing the g-force
                    drawCircle(
                        color = Color.Red,
                        radius = 10f,
                        center = center + Offset(dotX, -dotY)
                    )
                }
            }
        }

        //endregion

    //region Bluetooth interaction

    @ExperimentalMaterial3Api
    @Composable
    fun BtDeviceSelectionDialog(store: Store, btConnectionHandler: BtConnectionHandler) {
        val state = store.subject.map { it.tripViewModel }.subscribeAsState(TripViewModel())
        if (state.value.showDeviceSelectionDialog) {
            BasicAlertDialog(
                onDismissRequest = { state.value.showDeviceSelectionDialog = false },
            ) {
                Column(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(Modifier.padding(20.dp)) {
                        Text("Select a Bluetooth device")
                    }
                    ListPairedBtDevices(store = store, btConnectionHandler)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(onClick = {
                            store.next {
                                it.tripViewModel.showDeviceSelectionDialog = false
                            }
                        }) {
                            Text(text = "Close")
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    @Composable
    fun ConnectionInfo(store: Store, btConnectionHandler: BtConnectionHandler) {
        val state = store.subject.map { it.tripViewModel }.subscribeAsState(TripViewModel())
        var connectionStateColor = Color.Red

        store.subject.map { it.tripViewModel }.subscribe {
            connectionStateColor = when {
                it.connectionStateString == "Connected" -> Color.Green
                it.connectionStateString.contains("Connecting") -> Color.Yellow
                else -> Color.Red
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Button(
                    onClick = { startTrip() },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = "Start Trip", fontSize = 14.sp)
                }
                Button(
                    onClick = { stopTrip() },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = "Stop Trip", fontSize = 14.sp)
                }
            }
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Button(
                    onClick = { btConnectionHandler.createConnection(state.value.selectedDevice) },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = "Connect", fontSize = 14.sp)
                }
                Button(
                    onClick = {
                        Log.d(TAG, "Show Bt-Device selection dialog")
                        store.next { it.tripViewModel.showDeviceSelectionDialog = true }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = "Select Device", fontSize = 14.sp)
                }
            }
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Connection:",
                        modifier = Modifier.padding(1.dp),
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "Device:",
                        modifier = Modifier.padding(1.dp),
                        fontSize = 12.sp,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = state.value.connectionStateString,
                        modifier = Modifier.padding(1.dp),
                        fontSize = 12.sp,
                        color = connectionStateColor
                    )
                    Text(
                        text = state.value.selectedDevice?.name ?: "None",
                        modifier = Modifier.padding(5.dp),
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }

    @Composable
    fun ListPairedBtDevices(store: Store, btConnectionHandler: BtConnectionHandler) {
        LazyColumn(
            Modifier
                .padding(20.dp)
                .height(400.dp)
        ) {
            items(btConnectionHandler.getPairedDevices().size) { index ->
                BluetoothDeviceItem(btConnectionHandler.getPairedDevices().elementAt(index), store)
            }
        }
    }

    @Composable
    fun BluetoothDeviceItem(device: BtDevice, store: Store) {
        Log.d(TAG, "Device: ${device.name}")
        TextButton(
            modifier = Modifier
                .border(
                    1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.small
                )
                .fillMaxSize(),
            onClick = {
                store.next { it.tripViewModel.selectedDevice = device }
                store.next { it.tripViewModel.showDeviceSelectionDialog = false }
            },
        ) {
            Row {
                Text(text = device.name ?: "Unknown Device")
            }
        }
    }

//endregion

//region Testing OBD commands

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowTestObdCommandsCard(store: Store) {
        val state = store.subject.map { it.tripViewModel }.subscribeAsState(TripViewModel())

        //Don't show the dialog if the device is not connected
        if (state.value.showTestCommandDialog && !state.value.isConnected) {
            ShowAlert(
                title = "Not connected",
                text = "Please connect to a device first",
                onDismiss = {
                    obdReaderKt.cancelTest()
                    store.next { it.tripViewModel.showTestCommandDialog = false }
                })
        }
        //If the device is connected, but a trip has been started testing the commands is not available
        if (state.value.showTestCommandDialog && tripActive) {
            ShowAlert(
                title = "Trip active",
                text = "Please stop the trip first",
                onDismiss = {
                    store.next { it.tripViewModel.showTestCommandDialog = false }
                })
        }
        if (state.value.showTestCommandDialog && state.value.isConnected && !tripActive) {
            //only call this on the first composition
            LaunchedEffect(key1 = state.value.showTestCommandDialog) {
                if (state.value.showTestCommandDialog) {
                    store.next { it.tripViewModel.obdTestCommandResults.clear() }
                    obdReaderKt.testRelevantCommands(
                        btConnectionHandler.inputStream,
                        btConnectionHandler.outputStream
                    )
                }
            }

            BasicAlertDialog(
                onDismissRequest = {
                    store.next {
                        it.tripViewModel.showTestCommandDialog = false
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                Column(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(Modifier.padding(20.dp)) {
                        Text("Test Command Results")
                    }

                    LazyColumn(
                        Modifier
                            .padding(20.dp)
                            .height(600.dp)
                    ) {
                        items(state.value.obdTestCommandResults.entries.size) { index ->
                            Surface(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.onBackground,
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    if (state.value.obdTestCommandResults.size > index) {
                                        val value =
                                            state.value.obdTestCommandResults.entries.elementAt(
                                                index
                                            )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = value.key,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {

                                            Text(
                                                text = value.value,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(onClick = {
                            store.next {
                                it.tripViewModel.showTestCommandDialog = false
                            }
                        }) {
                            Text(text = "Close")
                        }
                    }
                }
            }
        }
    }

//endregion

//region map

    @ExperimentalMaterial3Api
    @Composable
    fun ShowMapCard(store: Store) {
        val state = store.subject.map { it.tripViewModel.map }.subscribeAsState(Map())
        if (state.value.showMap) {
            BasicAlertDialog(
                onDismissRequest = { store.next { it.tripViewModel.map.showMap = false } },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                store.subject.value?.tripViewModel?.map?.let {
                    ShowMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        latLngList = it.latLngList
                    )
                }
                Column {
                    OutlinedButton(
                        onClick = {
                            store.next { it.tripViewModel.map.showMap = false }
                            Log.d("MapCloseButton", "Clicked")
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp),
                        shape = CircleShape,
                        border = BorderStroke(3.dp, Color.Black),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

//endregion
}




