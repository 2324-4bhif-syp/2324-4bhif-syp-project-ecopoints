package at.htl.ecopoints.io

import android.util.Log
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.CarDataBackend
import at.htl.ecopoints.model.CarSensorData
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.service.TripService
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
import java.sql.Timestamp
import java.util.UUID
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
    lateinit var tripService: TripService

    @Inject
    constructor() {
    }

    var setupSteps = listOf<String>(
        "Initializing OBD-Adapter",
        "Looking up available commands for this car",
        "Finished getting available commands",
        "Cleanup",
        "Finished"
    )

    // OBD commands that the current connected Vehicle is capable of.
    // Set after calling checkAvailablePIDsAndCommands()
    var currentlySupportedCommands = listOf<ObdCommand>()

    suspend fun setupELM(inputStream: InputStream, outputStream: OutputStream) {
        try {
            outputStream.flush();
            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            obdSetupCommands.forEach { command ->
                try {
                    Log.i(TAG, "Running command ${command.name}")
                    var result = obdConnection.run(command, false, 200)
                    Log.d(TAG, buildObdResultLog(result))

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Error running OBD2 command ${command.name} setting up OBD-Adapter",
                        e
                    )
                }
                sleep(200)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error on OBD-Adapter setup", e)
        } finally {
            Log.i(TAG, "OBD-Adapter setup finished")
        }
    }

    suspend fun getAvailablePIDsAndCommands(
        inputStream: InputStream?,
        outputStream: OutputStream?
    ) {
        var workingCommands = mutableListOf<ObdCommand>()
        val availablePIDs = mutableListOf<ObdResponse>()

        var maxRetries = 10;
        try {

            setupELM(inputStream!!, outputStream!!)
            val obdConnection = ObdDeviceConnection(inputStream, outputStream)
            store.next {
                it.tripViewModel.elmSetupCurrentStep = setupSteps[0]
            }
            store.next { it.tripViewModel.elmSetupCurrentStep = setupSteps[1] }
            obdAvailablePIDsCommands.forEach { command ->
                var attempts = 0;
                var isSuccessful = false;

                while (attempts <= maxRetries && !isSuccessful) {
                    try {
                        Log.i(
                            CHECK_AVAILABLE_COMMAND_PIDS_TAG,
                            "Running command ${command.name}"
                        )

                        val result = obdConnection.run(command, false, 1000, 0)

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
                        Log.d(
                            CHECK_AVAILABLE_COMMAND_PIDS_TAG,
                            "Retrying command ${command.name}"
                        )
                    }
                }
            }
            store.next { it.tripViewModel.elmSetupCurrentStep = setupSteps[2] }
            sleep(1000)
            store.next { it.tripViewModel.elmSetupCurrentStep = setupSteps[3] }
            while (inputStream.available() > 0)
            {
                inputStream.read()
            }
            setupELM(inputStream,outputStream)
            store.next { it.tripViewModel.isSetupFinished = true }
            sleep(1000)

        } catch (e: Exception) {
            Log.e(
                CHECK_AVAILABLE_COMMAND_PIDS_TAG,
                "Error while setting up OBD connection",
                e
            )
        }
        workingCommands = relevantObdCommands.filter { command ->
            availablePIDs.any { pid ->
                pid.value.split(",").contains(command.pid)
            }
        }.toMutableList()

        Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Available PIDs are:")
        availablePIDs.forEach { pid ->
            Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, pid.formattedValue)
            store.next {
                it.tripViewModel.availablePIDSs[pid.command.name] = pid.value
            }
        }

        Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Available Commands are:")

        workingCommands.forEach { command ->
            Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, command.name)
            store.next { it ->
                it.tripViewModel.availableCommands[command.name] = "available"
            }
        }

        Log.d(CHECK_AVAILABLE_COMMAND_PIDS_TAG, "Setting currently supported commands")
        currentlySupportedCommands = workingCommands
    }

    fun stopCheckingAvailablePIDs() {
        getAvailablePIDsScope.cancel()
        getAvailablePIDsScope = CoroutineScope(Dispatchers.IO)
        sleep(1000)
        currentlySupportedCommands = listOf<ObdCommand>()
    }


    fun stopReading() {
        scope.cancel()
        scope = CoroutineScope(Dispatchers.IO)
        sleep(1000)
        writer.endJsonFile()

        val carSensorData = store.subject.value!!.tripViewModel.carData.toCarSensorData()
        println("CarSensorDataObject: $carSensorData")

        tripService.createTrip().thenAccept { tripId ->
            tripService.addDataToTrip(UUID.fromString(tripId), listOf(carSensorData)).thenAccept { response ->
                println(response)
            }
        }
    }

    fun Map<String, String>.toCarSensorData(): CarSensorData {
        var carSensorData = CarSensorData()
        carSensorData.timestamp = Timestamp(System.currentTimeMillis())
        var carData = CarDataBackend()
        carData.latitude = this["Latitude"]?.toDoubleOrNull() ?: 0.0
        carData.longitude = this["Longitude"]?.toDoubleOrNull() ?: 0.0
        carData.altitude = this["Altitude"]?.toDoubleOrNull() ?: 0.0
        carData.engineLoad = this["Engine Load"]?.toDoubleOrNull() ?: 0.0
        carData.coolantTemperature = this["Engine Coolant Temperature"]?.toDoubleOrNull() ?: 0.0
        carData.engineRpm = this["Engine RPM"]?.toDoubleOrNull() ?: 0.0
        carData.gpsSpeed = this["Gps-Speed"]?.toDoubleOrNull() ?: 0.0
        carData.obdSpeed = this["Vehicle Speed"]?.toDoubleOrNull() ?: 0.0
        carSensorData.carData = carData

        return carSensorData
    }

    fun startReading(
        inputStream: InputStream?,
        outputStream: OutputStream?,
    ) {
        writer.clearFile()
        writer.startJsonFile()

//        if (currentlySupportedCommands.isEmpty()) {
//            Log.e(TAG, "No OBD commands available")
//            getAvailablePIDsAndCommands(inputStream, outputStream)
//        }

        scope.launch() {
            try {
                getAvailablePIDsAndCommands(inputStream, outputStream)

                sleep(1000)
                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)
                currentlySupportedCommands = currentlySupportedCommands.reversed()

                var commandErrorMap = HashMap<String, Int>()
                val commandCounterMap = mutableMapOf<String, Int>().apply {
                    relevantObdCommands.forEach { command ->
                        put(command.name, 0) // Start each command's counter at 0
                    }
                }

                while (inputStream.available() > 0)
                {
                   inputStream.read()
                }

                while (isActive) {
                    currentlySupportedCommands.forEach { command ->
//                        val frequency =
//                            runFrequencyMap[command] ?: 1 // Default to run every iteration
//                        val counter = commandCounterMap[command.name] ?: 0
//
//                        if (counter >= frequency)
                        if (commandErrorMap.getOrDefault(command.name, 0) > 15) {
                            Log.e(
                                TAG,
                                "Command ${command.name} failed more than 10 times. Skipping command"
                            )
                        } else
                            try {
                                Log.i(TAG, "Running command ${command.name}")


                                val result = obdConnection.run(command, false, 5)

                                if(result.rawResponse.value == "7F0112"){
                                    throw Exception("Error running OBD2 command ${command.name}")
                                }

                                Log.d(TAG, buildObdResultLog(result))



                                store.next { it ->
                                    it.tripViewModel.carData[command.name] =
                                        result.value
//                                    Random.nextInt(2000).toString()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error running OBD2 command ${command.name}", e)
                                commandErrorMap[command.name] =
                                    commandErrorMap.getOrDefault(command.name, 0) + 1
                            }
                        // Reset the counter after running the command
                        commandCounterMap[command.name] = 0
                    }
                    while (inputStream.available() > 0)
                    {
                        inputStream.read()
                    }
                    // Take a snapshot and convert to JSON
                    val snapshot =
                        store.subject.value!!.tripViewModel.carData.map { (key, value) ->
                            "\"$key\": \"$value\""
                        }.joinToString(", ", "{", "}")

                    val timestamp = System.currentTimeMillis()
                    val jsonSnapshot = "{\n\"timestamp\": $timestamp,\n\"data\": $snapshot\n},"

//                    if (scope.isActive) writeDataSnapshotToFile(jsonSnapshot)
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