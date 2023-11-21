package at.htl.ecopoints.service

import android.bluetooth.BluetoothSocket
import android.util.Log
import at.htl.ecopoints.OBDBluetoothInterface
import at.htl.ecopoints.OBDCommandHelper
import at.htl.ecopoints.command.LoadCommand
import at.htl.ecopoints.command.RPMCommand
import at.htl.ecopoints.command.SpeedCommand
import java.io.IOException

class Obd2Service(bluetoothDeviceAddress: String) {

    private var isConnected: Boolean = false
    private val deviceAddress: String = bluetoothDeviceAddress
    private var obdBluetoothInterface: OBDBluetoothInterface? = null
    private val TAG: String = "Obd2Service"

    private suspend fun establishCommunicationToDevice() {
        try {
            val bluetoothService = BluetoothService()
            val bluetoothDeviceListService = BluetoothDeviceListService()
            bluetoothService.connectDevice(
                bluetoothDeviceListService.getDeviceByAddress(
                    deviceAddress
                )!!
            )
            val socket: BluetoothSocket = bluetoothService.getSocket()

            if (!bluetoothService.connected()) {
                isConnected = false
                Log.e(TAG, "Socket is not connected")
            } else if (bluetoothService.connected()) {
                isConnected = true
                Log.d(TAG, "Socket is connected")
            }

            obdBluetoothInterface = OBDBluetoothInterface(socket)


            Log.d(TAG, "Input and Output Stream created")

        } catch (e: IOException) {
            Log.e(
                TAG,
                "Error while establishing communication to device with Address $deviceAddress"
            )
            Log.e(TAG, e.toString())
        }
    }

    private suspend fun connect() {
        val tries = 3

        for (i in 1..tries) {
            Log.d(TAG, "Trying to connect to device $deviceAddress try $i")
            if (!isConnected) {
                break
            }
            establishCommunicationToDevice()
        }
    }

    suspend fun getApiV2RPM(): String {
        connect()
        if (isConnected) {
            return ""
        }


        try {
            Log.d(TAG, "Trying to get RPM")
            val response = obdBluetoothInterface!!.run(RPMCommand(), delayTime = 500L)
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

    suspend fun geApiV2Load(): String {
        connect()
        if (!isConnected) {
            return ""
        }


        try {
            Log.d(TAG, "Trying to get RPM")
            val response = obdBluetoothInterface!!.run(LoadCommand(), delayTime = 500L)
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

    suspend fun getApiV2Speed(): String {
        connect()
        if (!isConnected) {
            return ""
        }


        try {
            Log.d(TAG, "Trying to get RPM")
            val response = obdBluetoothInterface!!.run(SpeedCommand(), delayTime = 500L)
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

    suspend  fun initOBD() {
        connect()
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        obdCommandHelper.resetOBDSys()
        obdCommandHelper.echoOff()
        obdCommandHelper.headerOn()
    }

    suspend fun getRPM(): String {
        connect()
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        val rpm = obdCommandHelper.getEngineRPM()
        Log.d(TAG, "RPM: $rpm")
        return rpm
    }

    suspend fun getSpeed(): String {
        connect()
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        val speed = obdCommandHelper.getVehicleSpeed()
        Log.d(TAG, "Speed: $speed")
        return speed
    }

    suspend fun getCoolantTemp(): String {
        connect()
        val obdCommandHelper = OBDCommandHelper(obdBluetoothInterface!!)
        val coolantTemp = obdCommandHelper.getCoolantTemperature()
        Log.d(TAG, "Coolant-temp: $coolantTemp")
        return coolantTemp
    }
}