package at.htl.ecopoints

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import at.htl.ecopoints.command.ObdCommand
import at.htl.ecopoints.command.ObdRawResponse
import at.htl.ecopoints.command.ObdResponse
import at.htl.ecopoints.command.ObdResponseRegexPatterns
import at.htl.ecopoints.command.removeAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.system.measureTimeMillis

class OBDBluetoothInterface(private val bluetoothSocket: BluetoothSocket) {

    private val TAG = "OBDBluetoothInterface"
    private val inputStream: InputStream = bluetoothSocket.inputStream
    private val outputStream: OutputStream = bluetoothSocket.outputStream
    private val responseCache = mutableMapOf<ObdCommand, ObdRawResponse>()

    @SuppressLint("MissingPermission")
    fun sendCommand(command: String): String {
        try {
            outputStream.write(command.toByteArray(Charset.defaultCharset()))
            outputStream.write('\r'.code)
            val buffer = ByteArray(8000)
            val bytesRead = inputStream.read(buffer)
            val response = String(buffer, 0, bytesRead, Charset.defaultCharset()).trim()

            Log.d(TAG, "Command: $command, Response: $response")

            return response
        } catch (e: IOException) {
            Log.e(TAG, "Error sending/receiving OBD command: ${e.message}")
            bluetoothSocket.connect()
            return ""
        }
    }

    suspend fun run(
        command: ObdCommand,
        useCache: Boolean = false,
        delayTime: Long = 0,
        maxRetries: Int = 5,
    ): ObdResponse = runBlocking {
        val obdRawResponse =
            if (useCache && responseCache[command] != null) {
                responseCache.getValue(command)
            } else {
                runCommand(command, delayTime, maxRetries).also {
                    // Save response to cache
                    if (useCache) {
                        responseCache[command] = it
                    }
                }
            }
        command.handleResponse(obdRawResponse)
    }

    private suspend fun runCommand(command: ObdCommand, delayTime: Long, maxRetries: Int): ObdRawResponse {
        var rawData = ""
        val elapsedTime = measureTimeMillis {
            sendCommand(command, delayTime)
            rawData = readRawData(maxRetries)
        }
        return ObdRawResponse(rawData, elapsedTime)
    }

    private suspend fun sendCommand(command: ObdCommand, delayTime: Long) = runBlocking {
        withContext(Dispatchers.IO) {
            outputStream.write("${command.rawCommand}\r".toByteArray())
            outputStream.flush()
            if (delayTime > 0) {
                delay(delayTime)
            }
        }
    }

    private suspend fun readRawData(maxRetries: Int): String = runBlocking {
        var b: Byte
        var c: Char
        val res = StringBuffer()
        var retriesCount = 0

        withContext(Dispatchers.IO) {
            while (retriesCount <= maxRetries) {
                if (inputStream.available() > 0) {
                    b = inputStream.read().toByte()
                    if (b < 0) {
                        break
                    }
                    c = b.toInt().toChar()
                    if (c == '>') {
                        break
                    }
                    res.append(c)
                } else {
                    retriesCount += 1
                    delay(500)
                }
            }
            removeAll(ObdResponseRegexPatterns.SEARCHING_PATTERN, res.toString()).trim()
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
