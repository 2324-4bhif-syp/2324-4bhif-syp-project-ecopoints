package at.htl.ecopoints.service

import android.bluetooth.BluetoothSocket
import android.util.Log
import at.htl.ecopoints.OBDBluetoothInterface
import at.htl.ecopoints.command.OBDCommandHelper
import com.github.eltonvs.obd.command.engine.LoadCommand
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import com.github.eltonvs.obd.command.engine.RPMCommand
import com.github.eltonvs.obd.command.engine.SpeedCommand
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class Obd2Service(bluetoothDeviceAddress: String) {

    private val deviceAddress: String = bluetoothDeviceAddress
    private var connection: ObdDeviceConnection? = null
    private var obdBluetoothInterface: OBDBluetoothInterface? = null
    private val TAG: String = "Obd2Service"
    var socket: BluetoothSocket? = null

    private fun establishCommunicationToDevice(): ObdDeviceConnection? {
        try {
            val bluetoothService = BluetoothService()
            val bluetoothDeviceListService = BluetoothDeviceListService()
            bluetoothService.connectDevice(
                bluetoothDeviceListService.getDeviceByAddress(
                    deviceAddress
                )!!
            )
            val socket: BluetoothSocket = bluetoothService.getSocket()
            this.socket = socket

            if (!bluetoothService.connected()) {
                Log.e(TAG, "Socket is not connected")
                return null
            } else if (bluetoothService.connected()) {
                Log.d(TAG, "Socket is connected")
            }

            obdBluetoothInterface = OBDBluetoothInterface(socket)

            val inputStream: InputStream = socket.inputStream
            val outputStream: OutputStream = socket.outputStream

            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            Log.d(TAG, "Input and Output Stream created")
            return obdConnection

        } catch (e: IOException) {
            Log.e(
                TAG,
                "Error while establishing communication to device with Address $deviceAddress"
            )
            Log.e(TAG, e.toString())
        }
        return null
    }

    fun getSocketTest(): BluetoothSocket {
        return socket!!;
    }

    private fun connect() {
        val tries = 3

        for (i in 1..tries) {
            Log.d(TAG, "Trying to connect to device $deviceAddress try $i")
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
            Log.d(TAG, "RPM RAW: $response.rawResponse")
            Log.d(TAG, "RPM: ${response.value}")
            Log.d(TAG, "RPM: ${response.unit}")
            Log.d(TAG, "RPM: ${response.formattedValue}")
            return response.value

        } catch (e: IOException) {
            Log.e(
                "Obd2Service",
                "Error while establishing communication to device with UUID $deviceAddress"
            )
            Log.e("Obd2Service", e.toString())
        }
        return ""
    }

    suspend fun getEltonApiLoad(): String {
        connect()
        if (connection == null) {
            return ""
        }


        try {
            Log.d(TAG, "Trying to get RPM")
            val response = connection!!.run(LoadCommand(), delayTime = 500L)
            Log.d(TAG, "RPM RAW: $response.rawResponse")
            Log.d(TAG, "RPM: ${response.value}")
            Log.d(TAG, "RPM: ${response.unit}")
            Log.d(TAG, "RPM: ${response.formattedValue}")
            return response.value

        } catch (e: IOException) {
            Log.e(
                "Obd2Service",
                "Error while establishing communication to device with UUID $deviceAddress"
            )
            Log.e("Obd2Service", e.toString())
        }
        return ""
    }

    suspend fun getEltonApiSpeed(): String {
        connect()
        if (connection == null) {
            return ""
        }


        try {
            Log.d(TAG, "Trying to get RPM")
            val response = connection!!.run(SpeedCommand(), delayTime = 500L)
            Log.d(TAG, "RPM RAW: $response.rawResponse")
            Log.d(TAG, "RPM: ${response.value}")
            Log.d(TAG, "RPM: ${response.unit}")
            Log.d(TAG, "RPM: ${response.formattedValue}")
            return response.value

        } catch (e: IOException) {
            Log.e(
                "Obd2Service",
                "Error while establishing communication to device with UUID $deviceAddress"
            )
            Log.e("Obd2Service", e.toString())
        }
        return ""
    }

    fun initOBD() {
        connect()
        if (obdBluetoothInterface != null) {

            val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
            obdCommandHelper.resetOBDSys()
            obdCommandHelper.echoOff()
            obdCommandHelper.headerOn()
        }
    }

    fun getRPM(): String {
        if (obdBluetoothInterface == null) {
            connect()
        }
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        val rpm = obdCommandHelper.getEngineRPM()
        Log.d(TAG, "RPM: $rpm")
        return rpm
    }

    fun getSpeed(): String {
        if (obdBluetoothInterface == null) {
            connect()
        }
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        val speed = obdCommandHelper.getVehicleSpeed()
        Log.d(TAG, "Speed: $speed")
        return speed
    }

    fun getCoolantTemp(): String {
        if (obdBluetoothInterface == null) {
            connect()
        }
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        val coolantTemp = obdCommandHelper.getCoolantTemperature()
        Log.d(TAG, "Coolant-temp: $coolantTemp")
        return coolantTemp
    }
}