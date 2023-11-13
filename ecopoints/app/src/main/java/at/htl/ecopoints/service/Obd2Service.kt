package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import com.github.eltonvs.obd.command.ObdResponse
import com.github.eltonvs.obd.command.engine.RPMCommand
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class Obd2Service {

    val deviceAdress: String;

    constructor(bluetoothDeviceAddress: String) {
        deviceAdress = bluetoothDeviceAddress
    }

    suspend fun establishCommunicationToDevice(): ObdDeviceConnection? {
        try {
            val bluetoothService = BluetoothService()
            val bluetoothDeviceListService  = BluetoothDeviceListService()
            bluetoothService.connectDevice(bluetoothDeviceListService.getDeviceByAddress(deviceAdress)!!)
            val socket: BluetoothSocket? = bluetoothService.getSocket()

            if (socket == null) {
                Log.e("Obd2Service", "Socket is null")
                return null
            }
            else if (!socket.isConnected) {
                Log.e("Obd2Service", "Socket is not connected")
                return null
            }

            val inputStream: InputStream = socket.inputStream
            val outputStream: OutputStream = socket.outputStream

            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            return obdConnection;

        } catch (e: IOException) {
            Log.e(
                "Obd2Service",
                "Error while establishing communication to device with UUID $deviceAdress"
            )
            Log.e("Obd2Service", e.toString())
        }
        return null;
    }

    suspend fun getRpm(): String {
        val connection = establishCommunicationToDevice()

        if (connection == null) {
            return ""
        }

        val response: ObdResponse = connection.run(RPMCommand())
        return response.value;
    }
}