package at.htl.ecopoints.io

import android.util.Log
import at.htl.ecopoints.model.Store
import com.github.eltonvs.obd.command.ObdCommand
import com.github.eltonvs.obd.command.ObdResponse
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ObdReaderKt {
    private val TAG = ObdReader::class.java.getSimpleName()
    private val CHECK_AVAILABLE_COMMAND_PIDS_TAG = "CheckAvailableCommands"

    var scope = CoroutineScope(Dispatchers.IO)
    var getAvailablePIDsScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var store: Store

    @Inject
    lateinit var writer: JsonFileWriter

    @Inject
    constructor() {
    }

    // OBD commands that the current connected Vehicle is capable of.
    // Set after calling checkAvailablePIDsAndCommands()
    var currentlySupportedCommands = listOf<ObdCommand>()

    suspend fun setupELM(obdConnection: ObdDeviceConnection) {
        try {
            obdSetupCommands.forEach { command ->
                try {
                    Log.i(TAG, "Running command ${command.name}")
                    var result = obdConnection.run(command, false, 500)
                    Log.d(TAG, buildObdResultLog(result))

                } catch (e: Exception) {
                    Log.e(
                        CHECK_AVAILABLE_COMMAND_PIDS_TAG,
                        "Error running OBD2 command ${command.name} setting up OBD-Adapter",
                        e
                    )
                }
                delay(500)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error on OBD-Adapter setup", e)
        } finally {
            Log.i(TAG, "OBD-Adapter setup finished")
        }
    }

    fun checkAvailablePIDsAndCommands(inputStream: InputStream?, outputStream: OutputStream?) {
        try {
            val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)

            var (availableCommands, availablePIDs) = getAvailablePIDsAndCommands(obdConnection)

            Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Available PIDs are:")
            availablePIDs.forEach { pid ->
                Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, pid.formattedValue)
                store.next {
                    it.tripViewModel.availablePIDSs[pid.command.name] = pid.value
                }
            }

            Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Available Commands are:")

            availableCommands.forEach { command ->
                Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, command.name)
                store.next{it -> it.tripViewModel.availablePIDSs[command.name] = "available"}
            }

            Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Setting currently supported commands")
            currentlySupportedCommands = availableCommands

        } catch (e: Exception) {
            Log.e(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Error while setting up OBD connection", e)
        }
    }

    private fun getAvailablePIDsAndCommands(obdConnection: ObdDeviceConnection) : Pair<MutableList<ObdCommand>, MutableList<ObdResponse>> {
        var workingCommands = mutableListOf<ObdCommand>()
        val availablePIDs = mutableListOf<ObdResponse>()

        var maxRetries = 3;

        getAvailablePIDsScope.launch {
            setupELM(obdConnection)
            obdAvailablePIDsCommands.forEach { command ->
                var attempts = 0;
                var isSuccessful = false;

                while (attempts <= maxRetries && !isSuccessful) {
                    try {
                        Log.i(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Running command ${command.name}")

                        val result = obdConnection.run(command, false, 500, 0)

                        Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, buildObdResultLog(result))

                        availablePIDs.add(result)
                        isSuccessful = true;
                        attempts++;
                    } catch (e: Exception) {
                        attempts++;
                        Log.e(
                            CHECK_AVAILABLE_COMMAND_PIDS_TAG,
                            "Error running OBD2 command ${command.name} while getting available PIDs",
                            e
                        )
                        Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Retrying command ${command.name}")
                    }
                    delay(500)
                }
            }
            workingCommands = relevantObdCommands.filter { command ->
                availablePIDs.any { pid -> pid.command.pid == command.pid }
            }.toMutableList()
        }

        return Pair(workingCommands, availablePIDs)
    }

    fun stopCheckingAvailablePIDs() {
        getAvailablePIDsScope.cancel()
        getAvailablePIDsScope = CoroutineScope(Dispatchers.IO)
        sleep(1000)
        currentlySupportedCommands =listOf<ObdCommand>()
    }


    fun stopReading() {
        scope.cancel()
        scope = CoroutineScope(Dispatchers.IO)
        sleep(1000)
        writer.endJsonFile()
    }

    fun startReading(
        inputStream: InputStream?,
        outputStream: OutputStream?,
    ) {
        writer.clearFile()
        writer.startJsonFile()

        if(currentlySupportedCommands.isEmpty()) {
            Log.e(TAG, "No OBD commands available")
            checkAvailablePIDsAndCommands(inputStream, outputStream)
        }

        scope.launch() {
            try {
                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)
                setupELM(obdConnection)

                while (isActive) {
                    currentlySupportedCommands.forEach { command ->
                        try {
                            Log.i(TAG, "Running command ${command.name}")

                            writer.log(TAG + ": " + "Running command ${command.name}")

                            val result = obdConnection.run(command, false, 100, 5)
                            Log.d(TAG, buildObdResultLog(result))
                            writer.log(TAG + ": " + buildObdResultLog(result))

                            store.next { it ->
                                it.tripViewModel.carData[command.name] =
                                    result.value
//                                    Random.nextInt(2000).toString()
                            }
//                            delay(250)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error running OBD2 command ${command.name}", e)
                            writer.log(TAG + ": " + "Error running OBD2 command ${command.name}" + e);
                        }
                        delay(100)
                    }

                    // Take a snapshot and convert to JSON
                    val snapshot = store.subject.value!!.tripViewModel.carData.map { (key, value) ->
                        "\"$key\": \"$value\""
                    }.joinToString(", ", "{", "}")

                    val timestamp = System.currentTimeMillis()
                    val jsonSnapshot = "{\n\"timestamp\": $timestamp,\n\"data\": $snapshot\n},"

                    if (scope.isActive) writeDataSnapshotToFile(jsonSnapshot)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error while setting up OBD connection", e)
            }
        }
    }


    fun writeDataSnapshotToFile(data: String) {
        Log.d(TAG, "Writing data snapshot to file: $data")
        writer.appendJson(data)
    }

    fun buildObdResultLog(result: ObdResponse): String {
        var sb = StringBuilder()

        sb.appendLine("${result.command.name}[${result.command.pid}] Result:")
        sb.appendLine("Formatted value : ${result.formattedValue}" + " Value: ${result.value}" + " Raw response: ${result.rawResponse}")
        sb.appendLine("Unit: ${result.unit}")

        return sb.toString()
    }
}