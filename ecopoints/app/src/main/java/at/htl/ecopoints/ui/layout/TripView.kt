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
import at.htl.ecopoints.service.TripService
import at.htl.ecopoints.ui.component.ShowMap
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.fasterxml.jackson.databind.ObjectMapper
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt
import kotlin.random.Random

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
    lateinit var writer: JsonFileWriter

    @Inject
    lateinit var tripService: TripService

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
                    it.tripViewModel.carData["Gps-Speed"] =
                        ((location.speed * 10.0 * 3.6).roundToInt() / 10).toString()
//                        val fuelCons = generateRandomFuelCons()
//                        it.tripViewModel.map.add(
//                            location.latitude, location.longitude,
//                            fuelCons
//                        )
                }
            }

            val isDarkMode = store.subject.map { it.isDarkMode }.subscribeAsState(false)

            EcoPointsTheme(darkTheme = isDarkMode.value) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Main content
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
//                                        Button(
//                                            onClick = {
//                                                store.next {
//                                                    it.tripViewModel.showTestCommandDialog = true
//                                                }
//                                            },
//                                            modifier = Modifier.padding(end = 8.dp),
//                                            colors = ButtonDefaults.buttonColors(
//                                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                                            ),
//                                            shape = MaterialTheme.shapes.medium,
//                                            elevation = ButtonDefaults.buttonElevation(4.dp)
//                                        ) {
//                                            Text(
//                                                text = "Available Commands",
//                                                style = MaterialTheme.typography.bodyLarge
//                                            )
//                                        }

//                                        Button(onClick = {
//                                            val map: ConcurrentHashMap<String, String> =
//                                                ConcurrentHashMap<String, String>()
//                                            for (command in relevantObdCommands) {
//                                                map[command.name] =
//                                                    Random.nextDouble(0.0, 10000.0).toString();
//                                                Log.d(TAG, "Command: ${command.name} : Value: ${map[command.name]}")
//                                            }
//                                            map["Latitude"] = "34.34"
//                                            map["Longitude"] = "43.3"
//                                            map["Altitude"] = "2000"
//
//                                            var mapper = ObjectMapper();
//                                            try {
//                                                var jsonData = mapper.writeValueAsString(map.toCarSensorData());
//                                                Log.d(TAG, "c213411a-a205-42f6-bd1e-226a63a2bacb" + "Trip data: " + jsonData);
//                                            } catch (e : Exception) {
//                                                Log.e(TAG, "Failed to serialize trip data", e);
//                                            }
//
//                                            tripService.addDataToTrip(
//                                                UUID.fromString("c213411a-a205-42f6-bd1e-226a63a2bacb"),
//                                                listOf(map.toCarSensorData())
//                                            )
//                                                .thenAccept { response ->
//                                                    println(response)
//                                                }
//                                        }) {
//                                            Text(
//                                                text = "Test API"
//                                            )
//                                        }

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
                                    val (currentScreen, setCurrentScreen) = remember {
                                        mutableStateOf(
                                            "Trip"
                                        )
                                    }
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        BottomNavBar(
                                            currentScreen = currentScreen,
                                            onScreenSelected = { newScreen ->
                                                setCurrentScreen(
                                                    newScreen
                                                )
                                            },
                                            context = activity
                                        )
                                    }
                                }
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(it)
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    LiveCarData(
                                        store,
                                        context = activity.applicationContext
                                    )
                                }

                                BtDeviceSelectionDialog(store, btConnectionHandler)
                                ShowMapCard(store = store)
//                                ShowAvailableObdCommandsCard(store = store)
                            }
                        }
                    }

                    val setupState =
                        store.subject.map { it.tripViewModel.isSetupFinished }
                            .subscribeAsState(true)

                    val connectionState =
                        store.subject.map { it.tripViewModel.connectionStateString }
                            .subscribeAsState("Not connected")

                    val ecuState =
                        store.subject.map { it.tripViewModel.hasELMSetupAndCheckingForAvailableCommandsProcessStarted }
                            .subscribeAsState(true)

                    val elmSetupCurrentStepState =
                        store.subject.map { it.tripViewModel.elmSetupCurrentStep }
                            .subscribeAsState("Not Initialized")

                    val isConnectionActive = store.subject.map { it.tripViewModel.isConnected }
                        .subscribeAsState(false)

                    // Spinner overlay
                    if (!setupState.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp)) // Space between spinner and text

                                Text(
                                    text = "Please wait a moment.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )

                                //Bt Connection
                                Text(
                                    text = "Connecting to ELM327 Adapter: ${store.subject.value?.tripViewModel?.selectedDevice?.name}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = connectionState.value,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )

                                if (isConnectionActive.value) {
                                    LaunchedEffect(Unit) { // Using Unit as a key means it runs once per composition
                                        obdReaderKt.startReading(
                                            btConnectionHandler.inputStream,
                                            btConnectionHandler.outputStream
                                        )
                                    }
                                }


                                //ELM Setup and getting Available Commands
                                Text(
                                    text = "Setting up ELM327 Adapter:",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${elmSetupCurrentStepState.value}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }

//region Trip

    private fun stopTrip() {
        if (tripActive) {
            Log.i(TAG, "Trip stopped")
            store.next() {
                it.tripViewModel.tripActive = false
            }
//            obdReaderKt.stopReading()
            tripActive = false
        } else {
            Log.w(TAG, "Tried to stop a trip without starting one")
        }
    }

    private fun startTrip() {
        //View Testing
//        store.next{it.tripViewModel.isConnected = true}

        tripService.createTrip().thenAccept { id ->
            if (id != null)
                store.next() {
                    it.tripViewModel.tripActive = true;
                    it.tripViewModel.tripId = id.tripId
                    Log.i(
                        TAG,
                        "New Trip started, TripId: " +id.tripId
                    );
                }
        }
        if (store.subject.value?.tripViewModel?.isConnected == true) {
            Log.i(TAG, "Trip started")

            tripActive = true
        } else {
            Log.w(TAG, "Tried to start a trip without a connection to a device")
        }

    }

//endregion

//region Alerts

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

//endregion

//region LiveData Visual

    @SuppressLint("DefaultLocale")
    @Composable
    fun LiveCarData(store: Store, context: Context) {
        val isConnectedState =
            store.subject.map { it.tripViewModel.isConnected }.subscribeAsState(false)
        val state = store.subject.map { it.tripViewModel.carData }
            .subscribeAsState(ConcurrentHashMap<String, String>())

        LaunchedEffect(key1 = isConnectedState) {
//            store.next { store ->
//                obdReaderKt.carDataCommands.forEach {
//                    store.tripViewModel.carData[it.name] = "0"
//                }
//            }
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
                    value = (data["Latitude"]?.toFloatOrNull() ?: 0f).let {
                        String.format(
                            "%.1f",
                            it
                        )
                    },
                    icon = Icons.Default.LocationOn,
                    modifier = Modifier.weight(1f) // Ensure equal width
                )

                InfoCard2(
                    title = "Longitude",
                    value = (data["Longitude"]?.toFloatOrNull() ?: 0f).let {
                        String.format(
                            "%.1f",
                            it
                        )
                    },
                    icon = Icons.Default.LocationOn,
                    modifier = Modifier.weight(1f) // Ensure equal width
                )

                InfoCard2(
                    title = "Altitude",
                    value = "${
                        (data["Altitude"]?.toFloatOrNull() ?: 0f).let {
                            String.format(
                                "%.1f",
                                it
                            )
                        }
                    } m",
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

                // Draw inner circle
                drawCircle(color = Color.Gray, radius = radius / 2)

                // Draw X and Y axes
                drawLine(
                    color = Color.Black,
                    start = Offset(center.x, 0f),
                    end = Offset(center.x, size.height),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = 2f
                )

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
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    enabled = !tripActive && state.value.isConnected
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
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    enabled = tripActive
                ) {
                    Text(text = "Stop Trip", fontSize = 14.sp)
                }
            }
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Button(
                    onClick = {

                        if (state.value.selectedDevice != null) {
                            if (!state.value.isConnected) {
                                btConnectionHandler.createConnection(state.value.selectedDevice)
                                store.next { it.tripViewModel.isSetupFinished = false }
                            } else {

                                btConnectionHandler.disconnect()
                                tripActive = false
                                obdReaderKt.stopReading()
                            }
                        }
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
                    Text(
                        text = if (state.value.isConnected) "Disconnect" else "Connect",
                        fontSize = 14.sp
                    )
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

//region Available Obd-Commands

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowAvailableObdCommandsCard(store: Store) {
        val state = store.subject.map { it.tripViewModel }.subscribeAsState(TripViewModel())

        //Don't show the dialog if the device is not connected
        if (state.value.showTestCommandDialog && !state.value.isConnected) {
            ShowAlert(
                title = "Not connected",
                text = "Please connect to a device first",
                onDismiss = {
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
                    store.next { it.tripViewModel.availablePIDSs.clear() }
                    obdReaderKt.getAvailablePIDsAndCommands(
                        btConnectionHandler.inputStream,
                        btConnectionHandler.outputStream
                    )
                }
            }

            BasicAlertDialog(
                onDismissRequest = {
                    store.next {
                        obdReaderKt.stopCheckingAvailablePIDs()
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
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.shapes.extraLarge
                        )
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(Modifier.padding(20.dp)) {
                        Text("Available PIDs Are:")
                    }

                    // First LazyColumn for PIDs, no fixed height so it only takes needed space
                    LazyColumn(
                        modifier = Modifier
                            .padding(20.dp)
                            .wrapContentHeight() // Allows it to only use necessary space
                    ) {
                        items(state.value.availablePIDSs.entries.size) { index ->
                            Surface(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    val value = state.value.availablePIDSs.entries.elementAt(index)

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

                    // Command heading
                    Row(Modifier.padding(20.dp)) {
                        Text("Available Commands Are:")
                    }

                    // Second LazyColumn for commands with weight modifier to fill remaining space
                    LazyColumn(
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(1f) // Makes it expand to available space
                    ) {
                        items(state.value.availableCommands.entries.size) { index ->
                            Surface(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    val command =
                                        state.value.availableCommands.entries.elementAt(index)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = command.key,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = "available",
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Close button at the bottom
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.Center
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
    fun Map<String, String>.toCarSensorData(): CarSensorData {
        var carSensorData = CarSensorData()
        var timestamp: Timestamp = Timestamp(System.currentTimeMillis());

        var instant: Instant = timestamp.toInstant();
        var formattedTimestamp = instant.atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT);

        carSensorData.timestamp = formattedTimestamp
        var carData = CarDataBackend()
        carData.latitude = this["Latitude"]?.toDoubleOrNull() ?: 0.0
        Log.d(TAG, "Latitude: ${carData.latitude}")
        carData.longitude = this["Longitude"]?.toDoubleOrNull() ?: 0.0
        carData.altitude = this["Altitude"]?.toDoubleOrNull() ?: 0.0
        carData.engineLoad = this["Engine Load"]?.toDoubleOrNull() ?: 0.0
        carData.coolantTemperature = this["Engine Coolant Temperature"]?.toDoubleOrNull() ?: 0.0
        carData.engineRpm = this["Engine RPM"]?.toDoubleOrNull() ?: 0.0
        carData.gpsSpeed = this["Gps-Speed"]?.toDoubleOrNull() ?: 0.0
        carData.obdSpeed = this["Vehicle Speed"]?.toDoubleOrNull() ?: 0.0
        carSensorData.carData = carData

        return carSensorData
    }
}




