package at.htl.ecopoints.ui.layout

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import at.htl.ecopoints.model.*
import at.htl.ecopoints.io.*
import at.htl.ecopoints.model.viewmodel.TripViewModel
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.component.ShowMap
import at.htl.ecopoints.ui.component.Speedometer
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.io.LocationManager
import javax.inject.Inject
import javax.inject.Singleton

private val TAG = TripView::class.java.simpleName

@Singleton
class TripView {

    @Inject
    lateinit var store: Store

    @Inject
    lateinit var obdReader: ObdReader

    @Inject
    lateinit var obdReaderKt: ObdReaderKt

    @Inject
    lateinit var btConnectionHandler: BtConnectionHandler

    @Inject
    constructor()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CheckResult", "UnusedMaterial3ScaffoldPaddingParameter", "MissingPermission")
    fun compose(activity: ComponentActivity) {
        activity.setContent {

            LocationManager(activity.applicationContext) { location ->
                store.next {
                    it.tripViewModel.carData.latitude = location.latitude
                    it.tripViewModel.carData.longitude = location.longitude
                }
            }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Button(onClick = {}) {
                                Text(text = "Select your car")
                            }
                            Button(onClick = {
                                store.next { it.tripViewModel.map.showMap = true }
                            }) {
                                Text(text = "Map")
                            }
                        }
                    }, bottomBar = {
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
                    }) {

                        Spacer(modifier = Modifier.height(200.dp))
                        LiveCarData(store)
                        BtDeviceSelectionDialog(store, btConnectionHandler)
                        ShowMapCard(store = store)
                    }
                }
            }
        }
    }

    @Composable
    fun LiveCarData(store: Store) {
        val state = store.subject.map { it.tripViewModel.carData }.subscribeAsState(CarData())
        Column {
            Speedometer(
                currentSpeed = state.value.speed.toFloat(),
                modifier = Modifier
                    .padding(90.dp)
                    .requiredSize(250.dp)
            )
            Row {
                Column {
                    Text(text = "Latitude: ${state.value.latitude}")
                    Text(text = "Longitude: ${state.value.longitude}")
                    Text(text = "Rpm: ${state.value.currentEngineRPM}")
                    Text(text = "ThrPos: ${state.value.throttlePosition}")
                    Text(text = "EngineRt: ${state.value.engineRunTime}")
                    Text(text = "timestamp: ${state.value.timeStamp}")
                }
            }
        }
    }

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

    @SuppressLint("MissingPermission", "CheckResult")
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
            if (it.isConnected) {
                obdReaderKt.startReading(it.inputStream, it.outputStream)
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

    @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
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
                ShowMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
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
}
