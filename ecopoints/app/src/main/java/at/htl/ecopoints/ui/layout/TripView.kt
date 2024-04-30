package at.htl.ecopoints.ui.layout

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.Map
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.model.reading.BtConnectionHandler
import at.htl.ecopoints.model.reading.BtDevice
import at.htl.ecopoints.model.reading.ObdReader
import at.htl.ecopoints.model.viewmodel.TripViewModel
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.component.ShowMap
import at.htl.ecopoints.ui.component.Speedometer
import at.htl.ecopoints.ui.theme.EcoPointsTheme
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
    lateinit var btConnectionHandler: BtConnectionHandler

    @Inject
    constructor() {
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CheckResult", "UnusedMaterial3ScaffoldPaddingParameter")
    fun compose(activity: ComponentActivity) {
        activity.setContent {
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
                                obdReader.speedometerTest()
                            }) {
                                Text(text = "SpeedometerTest")
                            }
                            Button(onClick = {
                                store.next { it.tripViewModel.map.showMap = true }
                            }) {
                                Text(text = "Map")
                            }
                        }
                    }, bottomBar = {
                        Column(modifier = Modifier.padding(bottom = 50.dp)) {
                            ConnectionInfo(store, obdReader, btConnectionHandler)
                        }

                        Column {
                            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Trip") }
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ){

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
            Row() {
                Column {
                    Text(text = "Rpm: ${state.value.currentEngineRPM}")
                    Text(text = "ThrPos: ${state.value.throttlePosition}")
                    Text(text = "EngineRt: ${state.value.engineRunTime}")
                    Text(text = "timestamp: ${state.value.timeStamp}")
                    Text(text = "Latitude: ${state.value.latitude}")
                    Text(text = "Longitude: ${state.value.longitude}")
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
    fun ConnectionInfo(
        store: Store, obdReader: ObdReader, btConnectionHandler: BtConnectionHandler
    ) {
        val state = store.subject.map { it.tripViewModel }.subscribeAsState(TripViewModel())

        var connectionStateColor = Color.Red;

        store.subject.map { it.tripViewModel }.subscribe {
            if (it.connectionStateString == "Connected") {
                connectionStateColor = Color.Green
            } else if (it.connectionStateString.contains("Connecting")) {
                connectionStateColor = Color.Yellow
            } else {
                connectionStateColor = Color.Red
            }
            if (it.isConnected) {
                obdReader.startReading(it.inputStream, it.outputStream)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
            ) {
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
                        text = if (state.value.selectedDevice == null) {
                            "None"
                        } else {
                            state.value.selectedDevice.name
                        },
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
            Row() {
                Text(text = device.name ?: "Unknown Device")
            }
        }
    }

    //endregion

    @SuppressLint("MissingPermission")
    @ExperimentalMaterial3Api
    @Composable
    fun ShowMapCard(store: Store) {
        val state = store.subject.map { it.tripViewModel.map }.subscribeAsState(Map());
        if (state.value.showMap) {
            BasicAlertDialog(
                onDismissRequest = { store.next{ it -> it.tripViewModel.map.showMap = false } },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false)
            ) {
                ShowMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                Column {
                    OutlinedButton(
                        onClick = {
                            store.next { it -> it.tripViewModel.map.showMap = false };
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


