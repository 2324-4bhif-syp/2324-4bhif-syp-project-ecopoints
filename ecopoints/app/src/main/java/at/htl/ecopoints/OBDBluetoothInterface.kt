package at.htl.ecopoints

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

class OBDBluetoothInterface(private val bluetoothSocket: BluetoothSocket) {

    private val TAG = "OBDBluetoothInterface"
    private val inputStream: InputStream = bluetoothSocket.inputStream
    private val outputStream: OutputStream = bluetoothSocket.outputStream

    fun sendCommand(command: String): String {
        try {
            outputStream.write(command.toByteArray(Charset.defaultCharset()))
            outputStream.write('\r'.code) // Add carriage return

            val buffer = ByteArray(8000)
            val bytesRead = inputStream.read(buffer)
            val response = String(buffer, 0, bytesRead, Charset.defaultCharset()).trim()

            Log.d(TAG, "Command: $command, Response: $response")

            return response
        } catch (e: IOException) {
            Log.e(TAG, "Error sending/receiving OBD command: ${e.message}")
            return ""
        }
    }

    fun disconnect() {
        try {
            bluetoothSocket.close()
            Log.d(TAG, "Disconnected")
        } catch (e: IOException) {
            Log.e(TAG, "Error while disconnecting: ${e.message}")
        }
    }
}
