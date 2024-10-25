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
    private val TEST_COMMANDS_TAG = "OBD_COMMAND_TEST"

    var scope = CoroutineScope(Dispatchers.IO)
    var getAvailablePIDsScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var store: Store

    @Inject
    lateinit var writer: JsonFileWriter

    @Inject
    constructor() {
    }

    suspend fun setupELM(obdConnection: ObdDeviceConnection) {
        try {
            obdSetupCommands.forEach { command ->
                try {
                    Log.i(TAG, "Running command ${command.name}")
                    var result = obdConnection.run(command, false, 500)
                    Log.d(TAG, buildObdResultLog(result))

                } catch (e: Exception) {
                    Log.e(
                        TEST_COMMANDS_TAG,
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

    fun testRelevantCommands(inputStream: InputStream?, outputStream: OutputStream?) {
        try {
            val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)

            var availableCommands = getWorkingObdCommands(obdConnection)

            Log.d(TEST_COMMANDS_TAG, "Available Commands are:")

            availableCommands.forEach { command ->
                Log.d(TEST_COMMANDS_TAG, command.name)
                store.next{it -> it.tripViewModel.availablePIDSs[command.name] = "available"}
            }

        } catch (e: Exception) {
            Log.e(TEST_COMMANDS_TAG, "Error while setting up OBD connection", e)
        }

//                while (isActive)
//                    obdCommands.forEach { command ->
//                        try {
//                            Log.i(TEST_COMMANDS_TAG, "Running command ${command.name}")
//
//
//                            val result = obdConnection.run(command, false, 250, 0)
//
//                            Log.d(TEST_COMMANDS_TAG, buildObdResultLog(result))
//
//
//                            store.next { it ->
//                                it.tripViewModel.obdTestCommandResults[command.name] =
//                                    result.formattedValue
//                            }
//
//                        } catch (e: Exception) {
//                            Log.e(
//                                TEST_COMMANDS_TAG,
//                                "Error running OBD2 command ${command.name} while testing all commands",
//                                e
//                            )
//                            store.next { it ->
//                                it.tripViewModel.obdTestCommandResults[command.name] =
//                                    "Error running command"
//                            }
//                        }
//                        delay(500)
//                    }
//            } catch (e: Exception) {
//                Log.e(TEST_COMMANDS_TAG, "Error while setting up OBD connection", e)
//            } finally {
//                Log.i(TEST_COMMANDS_TAG, "OBD command test finished")

//        }
    }

    fun cancelTest() {
        getAvailablePIDsScope.cancel()
        getAvailablePIDsScope = CoroutineScope(Dispatchers.IO)
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
        scope.launch() {
            try {
                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)
                setupELM(obdConnection)

                while (isActive) {
                    carDataCommands.forEach { command ->
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

    fun getWorkingObdCommands(obdConnection: ObdDeviceConnection): List<ObdCommand> {
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
                        Log.i(TEST_COMMANDS_TAG, "Running command ${command.name}")

                        val result = obdConnection.run(command, false, 500, 0)

                        Log.d(TEST_COMMANDS_TAG, buildObdResultLog(result))

                        availablePIDs.add(result)
                        isSuccessful = true;
                        attempts++;
                    } catch (e: Exception) {
                        attempts++;
                        Log.e(
                            TEST_COMMANDS_TAG,
                            "Error running OBD2 command ${command.name} while getting available PIDs",
                            e
                        )
                        Log.d(TEST_COMMANDS_TAG, "Retrying command ${command.name}")
                    }
                    delay(500)
                }
            }

            Log.d(TEST_COMMANDS_TAG, "Available PIDs are:")
            availablePIDs.forEach { pid ->
                Log.d(TEST_COMMANDS_TAG, pid.formattedValue)
                store.next {
                    it.tripViewModel.availablePIDSs[pid.command.name] = pid.value
                }
            }
            workingCommands = relevantObdCommands.filter { command ->
                availablePIDs.any { pid -> pid.command.pid == command.pid }
            }.toMutableList()
        }

        return workingCommands
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