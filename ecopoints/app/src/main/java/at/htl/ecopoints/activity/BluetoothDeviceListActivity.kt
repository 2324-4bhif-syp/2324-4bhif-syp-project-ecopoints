package at.htl.ecopoints.activity

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import at.htl.ecopoints.service.BluetoothDeviceListService

class BluetoothDeviceListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothDeviceService = BluetoothDeviceListService()
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
                BluetoothDeviceList(bluetoothDeviceService.getAllDevices())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun useDevice(device: BluetoothDevice) {
        val intent = Intent(this@BluetoothDeviceListActivity, Obd2ReadingActivity::class.java)
        intent.putExtra("deviceName", device.name)
        intent.putExtra("deviceAddress", device.address)
        startActivity(intent)
    }


    @Composable
    fun BluetoothDeviceList(devices: List<BluetoothDevice>) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Paired Bluetooth Devices",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(devices) { device ->
                    BluetoothDeviceItem(device = device) {
                        useDevice(device)
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }
        }
    }

    @Composable
    fun BluetoothDeviceItem(device: BluetoothDevice, onItemClick: () -> Unit) {
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
                    Text(
                        text = "Name: ${device.name}",
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary)
                    )
                    Text(
                        text = "Address: ${device.address}",
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary)
                    )
                    Text(
                        text = "Bondstate ${device.bondState}",
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary)
                    )
                    Text(
                        text = "Type ${device.type}",
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary)
                    )
                    if(device.uuids != null){
                        Text(
                            text = "UUID ${device.uuids[0]}",
                            style = TextStyle(color = MaterialTheme.colorScheme.secondary)
                        )
                    }
                    Button(onClick = onItemClick) {
                        Text(text = "Use Device")
                    }
                }
            }
        }
    }
}
