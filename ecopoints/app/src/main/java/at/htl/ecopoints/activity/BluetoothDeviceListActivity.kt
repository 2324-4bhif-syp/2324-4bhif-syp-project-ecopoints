package at.htl.ecopoints.activity

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class BluetoothDeviceListActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        BluetoothAdapter.getDefaultAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    1
                )
            } else {
                bluetoothDeviceList(bluetoothAdapter?.bondedDevices?.toList() ?: emptyList())
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.d("Connect", "Try to get socket")
                val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(
                    UUID.fromString(device.uuids.get(0).toString())
                )

                if (ActivityCompat.checkSelfPermission(
                        this@BluetoothDeviceListActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@launch
                }

                Log.d("Connect", "Try to connect")
                socket.connect()

                if(socket.isConnected) {
                    Log.d("Bluetooth", "Connected")
                } else {
                    Log.e("Bluetooth", "Not Connected")
                }

                withContext(Dispatchers.Main) {

                }
            } catch (e: IOException) {
                Log.e("Connect", "Could not connect to device", e)
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

    @Composable
    fun bluetoothDeviceList(devices: List<BluetoothDevice>) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Paired Bluetooth Devices",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(devices) { device ->
                    bluetoothDeviceItem(device = device) {
                       connectToDevice(device)
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }
        }
    }

    @Composable
    fun bluetoothDeviceItem(device: BluetoothDevice, onItemClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                if (ActivityCompat.checkSelfPermission(
                        this@BluetoothDeviceListActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@BluetoothDeviceListActivity,
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        1
                    )
                } else {
                    Text(text = "Name: ${device.name}")
                    Text(text = "Address: ${device.address}")
                    //Text(text = "UUID: ${device.uuids.get(0)}")
                    Text(text = "Bondstate ${device.bondState}")
                    Button(onClick = onItemClick) {
                        Text(text = "Connect")
                    }
                }
            }
        }
    }
}

