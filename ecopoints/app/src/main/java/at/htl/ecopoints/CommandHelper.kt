package at.htl.ecopoints

import android.bluetooth.BluetoothSocket

class OBDCommandHelper(private val obdBluetoothInterface: OBDBluetoothInterface) {

    fun resetOBDSys(): String {
        val command = "ATZ"
        return obdBluetoothInterface.sendCommand(command)
    }

    fun headerOn(): String {
        val command = "ATH"
        return obdBluetoothInterface.sendCommand(command)
    }

    fun echoOff(): String {
        val command = "ATE0"
        return obdBluetoothInterface.sendCommand(command)
    }

    fun getEngineRPM(): String {
        val command = "01 0C"

        return obdBluetoothInterface.sendCommand(command)
    }

    fun getCoolantTemperature(): String {
        val command = "01 05"
        return obdBluetoothInterface.sendCommand(command)
    }

    fun getVehicleSpeed(): String {
        val command = "01 0D"
        return obdBluetoothInterface.sendCommand(command)
    }
}