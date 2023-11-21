package at.htl.ecopoints

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

        val rawData =  obdBluetoothInterface.sendCommand(command)

        try {
            val cleanData = rawData.replace("OK", "").replace("SEARCHING...", "").replace("SEARCHING:","")
            val array = cleanData.split(" ")

            if (array.size >= 4) {
                val combinedHex = array[2] + array[3]

                val combinedDecimal = combinedHex.toInt(16)
                val rpm = combinedDecimal / 4

                return rpm.toString()
            }
        } catch (_: Exception) {

        }

        return "0";
    }

    fun getCoolantTemperature(): String {
        val command = "01 05"
        val rawData =  obdBluetoothInterface.sendCommand(command)
        try {
            val cleanData = rawData.replace("OK", "").replace("SEARCHING...", "").replace("SEARCHING:","")
            val array = cleanData.split(" ")

            if (array.size >= 4) {
                val hex = array[3]

                val temp = hex.toInt(16) - 40

                return temp.toString()
            }
        } catch (_: Exception) {

        }

        return "0"
    }

    fun getVehicleSpeed(): String {
        val command = "01 0D"
        val rawData = obdBluetoothInterface.sendCommand(command)

        try {
            val cleanData = rawData.replace("OK", "").replace("SEARCHING...", "").replace("SEARCHING:","")
            val array = cleanData.split(" ")

            if (array.size >= 3) {
                val hex = array[2]

                val speed = hex.toInt(16)

                return speed.toString()
            }
        } catch (_: Exception) {

        }

        return "0"
    }
}