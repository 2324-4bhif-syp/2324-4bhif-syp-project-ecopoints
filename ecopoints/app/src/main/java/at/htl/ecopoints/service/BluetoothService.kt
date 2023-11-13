package at.htl.ecopoints.service

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService {

    private val  TAG = "BluetoothService"
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private lateinit var socket: BluetoothSocket

    public fun connected() = this::socket.isInitialized && socket.isConnected

    public fun getSocket() = socket

    @SuppressLint("MissingPermission")
    suspend fun connectDevice(device: BluetoothDevice) {
        withContext(Dispatchers.IO) {
            socket = device.createInsecureRfcommSocketToServiceRecord(uuid)
            try {
                if (socket.isConnected) {
                    socket.close()
                }
                socket.connect()
                Log.d(TAG, socket.isConnected.toString())
            } catch (e: IOException) {
                Log.e(TAG, "Error while connecting to device with UUID $device")
                Log.e(TAG, e.toString())
            }
        }
    }
}