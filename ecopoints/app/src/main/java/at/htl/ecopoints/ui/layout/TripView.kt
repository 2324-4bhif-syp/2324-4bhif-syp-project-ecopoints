package at.htl.ecopoints.ui.layout

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Button(onClick = {
                                    store.next { it.tripViewModel.map.showMap = true }
                                }) {
                                    Text(text = "Map")
                                }
                                Button(onClick = {
                                    store.next { it.tripViewModel.showTestCommandDialog = true }
                                }) {
                                    Text(text = "TestCommands")
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
                                LiveCarData(store)  // Display your car data here
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
    fun LiveCarData(store: Store) {
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

            // Row for Speed and RPM
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Gauge(title = "GPS-Speed", value = data["Gps-Speed"] ?: "0", unit = "km/h")
                Gauge(title = "Armin-Speed", value = data["Armin-Speed"] ?: "0", unit = "km/h")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row for Coolant Temp and Intake Temp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
//                Gauge(title = "RPM2", value = data["Engine RPM_CLEANED_RES"] ?: "0", unit = "rpm")
                Gauge(title = "Obd-Speed", value = data["Vehicle Speed"] ?: "0", unit = "km/h")
                Gauge(title = "RPM", value = data["Engine RPM"] ?: "0", unit = "rpm")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row for Engine Load and MAF
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Gauge(title = "Engine Load", value = data["Engine Load"] ?: "0", unit = "%")
                Gauge(title = "Throttle Position", value = data["Throttle Position"] ?: "0", unit = "%")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row for Distance, Driving Time, and Avg Speed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(title = "Engine Coolant Temperature", value = data["Engine Coolant Temperature"] ?: "0", unit = "°C")
                InfoCard(title = "Fuel Consumption Rate", value = data["Fuel Consumption Rate"] ?: "0", unit = "L/H")
                InfoCard(title = "Engine Oil Temperature", value = data["Engine Oil Temperature"] ?: "0", unit = "°C")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row for Fuel Efficiency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(title = "Latitude", value = data["Latitude"] ?: "0", unit = "")
                InfoCard(title = "Longitude", value = data["Longitude"] ?: "0", unit = "")
                InfoCard(title = "Altitude", value = data["Altitude"] ?: "0", unit = "")
            }
        }
    }

    // Components for the custom UI elements with size adjustments
    @Composable
    fun Gauge(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier
//                .padding(start = 20.dp, end = 20.dp, top = 3.dp, bottom = 3.dp)
                .height(70.dp)  // Height of the card
                .width(110.dp)  // Width of the card (make it longer)
                .clip(MaterialTheme.shapes.large) // Apply rounded corners
                .background(MaterialTheme.colorScheme.secondaryContainer),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ), // Shadow effect
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), // Fill the entire card
                contentAlignment = Alignment.Center // Center content both vertically and horizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // Center content vertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center // Center the title text
                    )
                    // Simulate a circular gauge here
                    Text(
                        text = "$value $unit",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center // Center the value text
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
    fun InfoCard(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(4.dp)
                .size(100.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall, fontSize = 12.sp)
            Text(
                text = "$value $unit",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = { startTrip() },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(text = "Start Trip")
                }
                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = { stopTrip() },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(text = "Stop Trip")
                }
            }
            Row {
                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = { btConnectionHandler.createConnection(state.value.selectedDevice) },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(text = "Connect")
                }
                Button(
                    shape = MaterialTheme.shapes.medium, onClick = {
                        Log.d(TAG, "Show Bt-Device selection dialog")
                        store.next { it.tripViewModel.showDeviceSelectionDialog = true }
                    }, modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp)
                ) {
                    Text(text = "Select Device")
                }
            }
            Row {
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
                    modifier = Modifier.weight(01f), horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = state.value.connectionStateString,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 12.sp,
                        color = connectionStateColor
                    )
                    Text(
                        text = state.value.selectedDevice?.name ?: "None",
                        modifier = Modifier.padding(8.dp),
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




