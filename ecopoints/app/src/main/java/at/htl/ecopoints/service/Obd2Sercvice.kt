package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import at.htl.ecopoints.activity.Obd2ReadingActivity
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import com.github.eltonvs.obd.command.ObdResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class Obd2Sercvice {

    val deviceUUID: UUID;

    constructor(bluetoothDeviceUUID: UUID) {
        deviceUUID = bluetoothDeviceUUID
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(): BluetoothSocket? {
        Log.d("Obd2Service", "Connecting to device with UUID $deviceUUID")
        val bluetoothDeviceService = BluetoothDeviceService()
        val device = bluetoothDeviceService.getDeviceByUUID(deviceUUID)
        try {
            val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(deviceUUID)
            socket.connect()
            return socket
        } catch (e: Exception) {
            Log.e("Obd2Service", "Error while connecting to device with UUID $deviceUUID")
            Log.e("Obd2Service", e.toString())
        }

        return null;
    }

    fun establishCommunicationToDevice(): ObdDeviceConnection? {
        try {

            val socket: BluetoothSocket? = connectToDevice();
            if (socket == null) {
                Log.e("Obd2Service", "Socket is null")
                return null
            }
            val inputStream: InputStream = socket.inputStream
            val outputStream: OutputStream = socket.outputStream

            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            return obdConnection;

        } catch (e: IOException) {
            Log.e(
                "Obd2Service",
                "Error while establishing communication to device with UUID $deviceUUID"
            )
            Log.e("Obd2Service", e.toString())
        }
        return null;
    }

    suspend fun getRpm() : String {
        val connection = establishCommunicationToDevice()

        if(connection == null){
            return ""
        }

        val response : ObdResponse =  connection.run(SpeedCommand())
        return response.value;
    }
}