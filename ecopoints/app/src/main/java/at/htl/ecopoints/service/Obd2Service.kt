package at.htl.ecopoints.service

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import com.github.eltonvs.obd.command.engine.RPMCommand
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class Obd2Service(bluetoothDeviceAddress: String) {

    val deviceAdress: String = bluetoothDeviceAddress
    var connection: ObdDeviceConnection? = null
    val TAG = "Obd2Service"

    suspend fun establishCommunicationToDevice(): ObdDeviceConnection? {
        try {
            val bluetoothService = BluetoothService()
            val bluetoothDeviceListService = BluetoothDeviceListService()
            bluetoothService.connectDevice(
                bluetoothDeviceListService.getDeviceByAddress(
                    deviceAdress
                )!!
            )
            val socket: BluetoothSocket = bluetoothService.getSocket()

            if (socket == null) {
                Log.e(TAG, "Socket is null")
                return null
            } else if (!bluetoothService.connected()) {
                Log.e(TAG, "Socket is not connected")
                return null
            }
            else if(bluetoothService.connected()){
                Log.d(TAG, "Socket is connected")
            }

            val inputStream: InputStream = socket.inputStream
            val outputStream: OutputStream = socket.outputStream

            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            Log.d(TAG, "Input and Output Stream created")
            return obdConnection

        } catch (e: IOException) {
            Log.e(
                TAG,
                "Error while establishing communication to device with Address $deviceAdress"
            )
            Log.e(TAG, e.toString())
        }
        return null;
    }

    suspend fun connect() {
        var tries = 3

        for (i in 1..tries) {
            Log.d(TAG, "Trying to connect to device $deviceAdress try $i")
            if (connection != null) {
                break
            }
            connection = establishCommunicationToDevice()
        }
    }

    suspend fun getEltonApiRPM(): String {
        connect()
        if (connection == null) {
            return ""
        }


        try {
            Log.d(TAG, "Trying to get RPM")
            val response = connection!!.run(RPMCommand(), delayTime = 500L)
            Log.d(TAG,"RPM RAW: $response.rawResponse")
            Log.d(TAG, "RPM: ${response.value}")
            Log.d(TAG, "RPM: ${response.unit}")
            Log.d(TAG, "RPM: ${response.formattedValue}")
            return response.value;

        } catch (e: IOException) {
            Log.e(
                "Obd2Service",
                "Error while establishing communication to device with UUID $deviceAdress"
            )
            Log.e("Obd2Service", e.toString())
        }
        return ""
    }
}