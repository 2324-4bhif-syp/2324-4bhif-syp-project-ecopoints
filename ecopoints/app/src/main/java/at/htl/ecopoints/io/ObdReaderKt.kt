package at.htl.ecopoints.io

import android.util.Log
import at.htl.ecopoints.model.Store
import com.github.eltonvs.obd.command.ObdCommand
import com.github.eltonvs.obd.command.ObdProtocols
import com.github.eltonvs.obd.command.ObdRawResponse
import com.github.eltonvs.obd.command.ObdResponse
import com.github.eltonvs.obd.command.Switcher
import com.github.eltonvs.obd.command.at.ResetAdapterCommand
import com.github.eltonvs.obd.command.at.SelectProtocolCommand
import com.github.eltonvs.obd.command.at.SetEchoCommand
import com.github.eltonvs.obd.command.at.SetHeadersCommand
import com.github.eltonvs.obd.command.at.SetLineFeedCommand
import com.github.eltonvs.obd.command.at.SetSpacesCommand
import com.github.eltonvs.obd.command.bytesToInt
import com.github.eltonvs.obd.command.control.AvailablePIDsCommand
import com.github.eltonvs.obd.command.control.VINCommand
import com.github.eltonvs.obd.command.engine.AbsoluteLoadCommand
import com.github.eltonvs.obd.command.engine.LoadCommand
import com.github.eltonvs.obd.command.engine.RPMCommand
import com.github.eltonvs.obd.command.engine.RelativeThrottlePositionCommand
import com.github.eltonvs.obd.command.engine.RuntimeCommand
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.command.engine.ThrottlePositionCommand
import com.github.eltonvs.obd.command.fuel.FuelAirEquivalenceRatioCommand
import com.github.eltonvs.obd.command.fuel.FuelConsumptionRateCommand
import com.github.eltonvs.obd.command.fuel.FuelLevelCommand
import com.github.eltonvs.obd.command.fuel.FuelTrimCommand
import com.github.eltonvs.obd.command.fuel.FuelTypeCommand
import com.github.eltonvs.obd.command.pressure.BarometricPressureCommand
import com.github.eltonvs.obd.command.pressure.FuelPressureCommand
import com.github.eltonvs.obd.command.pressure.FuelRailGaugePressureCommand
import com.github.eltonvs.obd.command.pressure.FuelRailPressureCommand
import com.github.eltonvs.obd.command.pressure.IntakeManifoldPressureCommand
import com.github.eltonvs.obd.command.temperature.AirIntakeTemperatureCommand
import com.github.eltonvs.obd.command.temperature.AmbientAirTemperatureCommand
import com.github.eltonvs.obd.command.temperature.EngineCoolantTemperatureCommand
import com.github.eltonvs.obd.command.temperature.OilTemperatureCommand
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
import kotlin.random.Random


@Singleton
class ObdReaderKt {
    private val TAG = ObdReader::class.java.getSimpleName()
    private val TEST_COMMANDS_TAG = "OBD_COMMAND_TEST"

    var scope = CoroutineScope(Dispatchers.IO)
    var testCommandScope = CoroutineScope(Dispatchers.IO)

    val obdSetupCommands = listOf<ObdCommand>(
        ResetAdapterCommand(),
        SetEchoCommand(Switcher.OFF),
        SetLineFeedCommand(Switcher.OFF),
        SetSpacesCommand(Switcher.OFF),
        SetHeadersCommand(Switcher.OFF),
        SelectProtocolCommand(ObdProtocols.AUTO),
        AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_01_TO_20),
        AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_21_TO_40),
        AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_41_TO_60),
        AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_61_TO_80),
        AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_81_TO_A0),
    )

    val obdCommands = listOf<ObdCommand>(
        RPMCommand(),
        SpeedCommand(),

        AbsoluteLoadCommand(),
        LoadCommand(),

        ThrottlePositionCommand(),
        RelativeThrottlePositionCommand(),

        EngineCoolantTemperatureCommand(),
        OilTemperatureCommand(),
        AirIntakeTemperatureCommand(),
        AmbientAirTemperatureCommand(),

        RuntimeCommand(),

        VINCommand(),

        FuelLevelCommand(),
        FuelTypeCommand(),
        FuelConsumptionRateCommand(),
        FuelRailPressureCommand(),
        FuelPressureCommand(),
        FuelRailGaugePressureCommand(),

        IntakeManifoldPressureCommand(),
        BarometricPressureCommand(),

        )

    val carDataCommands = listOf<ObdCommand>(
        RPMCommand(),
//        RpmCleanedResCommand(),
        SpeedCommand(),
//        FuelConsumptionRateCommand(),
//        LoadCommand(),
//        AbsoluteLoadCommand(),
//        ThrottlePositionCommand(),
//        RelativeThrottlePositionCommand(),
        EngineCoolantTemperatureCommand(),
        AirIntakeTemperatureCommand(),
        LoadCommand(),
//        OilTemperatureCommand(),
    )

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
        testCommandScope.launch { // Launch a coroutine in the scope
            try {
                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)
                setupELM(obdConnection)

                while (isActive)
                    obdCommands.forEach { command ->
                        try {
                            Log.i(TEST_COMMANDS_TAG, "Running command ${command.name}")


                            val result = obdConnection.run(command, false, 250, 0)

                            Log.d(TEST_COMMANDS_TAG, buildObdResultLog(result))


                            store.next { it ->
                                it.tripViewModel.obdTestCommandResults[command.name] =
                                    result.formattedValue
                            }

                        } catch (e: Exception) {
                            Log.e(
                                TEST_COMMANDS_TAG,
                                "Error running OBD2 command ${command.name} while testing all commands",
                                e
                            )
                            store.next { it ->
                                it.tripViewModel.obdTestCommandResults[command.name] =
                                    "Error running command"
                            }
                        }
                        delay(500)
                    }
            } catch (e: Exception) {
                Log.e(TEST_COMMANDS_TAG, "Error while setting up OBD connection", e)
            } finally {
                Log.i(TEST_COMMANDS_TAG, "OBD command test finished")
            }
        }
    }

    fun cancelTest() {
        testCommandScope.cancel()
        testCommandScope = CoroutineScope(Dispatchers.IO)
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
                                it.tripViewModel.carData[command.name] = result.value
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

//    fun startReading(inputStream: InputStream?, outputStream: OutputStream?) {
//        scope.launch {
//            try {
//                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)
//
//                setupELM(obdConnection)
//
//                while (isActive) {
//                    obdCommands.forEach { command ->
//                        try {
//                            val result = obdConnection.run(command, false, 0, 0)
//                            var sb = StringBuilder()
//
//                            sb.appendLine("${command.name} Result:")
//                            sb.appendLine("PID: $command.pid")
//                            sb.appendLine("Raw command: ${command.rawCommand}")
//                            sb.appendLine("Formatted value value: ${result.formattedValue}")
//                            sb.appendLine("Value: ${result.value}")
//                            sb.appendLine("Raw response: ${result.rawResponse}")
//                            sb.appendLine("Unit: ${result.unit}")
//
//                            val logMsg = sb.toString()
//
//                            Log.d(
//                                TAG, logMsg
//                            )
//
//                            delay(250)
//                        } catch (e: Exception) {
//                            //Log.e(TAG, "Error running OBD2 command ${command.name}", e)
//                        }
//                    }
//                    delay(1000)
//                }
//            } catch (e: Exception) {
//                //Log.e(TAG, "Error while setting up OBD connection", e)
//            }
//        }
//
//
////        //Setup
////        singleThreadExecutor.execute {
////            try {
////                val obdConnection = ObdDeviceConnection(
////                    inputStream!!,
////                    outputStream!!
////                )
////
////
////            } catch (e: Exception) {
////                Log.e(TAG, "Error while setting up OBD connection", e)
////            }
////        }
////
////
////        executor.scheduleAtFixedRate({
////            try {
////                val obdConnection = ObdDeviceConnection(
////                    inputStream!!,
////                    outputStream!!
////                )
////
////                val res: Any = obdConnection.run(RPMCommand(), false, 0, 0)
////                Log.d(TAG, "RPM: $res")
////            } catch (e: Exception) {
////                Log.e(TAG, "Error while setting up OBD connection", e)
////            }
////        }, 1, 300, TimeUnit.MILLISECONDS)
//    }

//    fun updateCarData(command: ObdCommand, result: ObdResponse) {
//        store.next {
//
//            try {
//
//                when (command) {
//                    is RPMCommand -> it.tripViewModel.carData.currentEngineRPM =
//                        result.value.toDouble()
//
//                    is SpeedCommand -> it.tripViewModel.carData.speed = result.value.toDouble()
//                    is ThrottlePositionCommand -> it.tripViewModel.carData.throttlePosition =
//                        result.value.toDouble()
//
//                    is RuntimeCommand -> it.tripViewModel.carData.engineRunTime =
//                        result.formattedValue
//                }
//            } catch (_: Exception) {
//            }
//            it.tripViewModel.carData.timeStamp = Timestamp(System.currentTimeMillis())
//
//        }
//    }

    fun buildObdResultLog(result: ObdResponse): String {
        var sb = StringBuilder()

        sb.appendLine("${result.command.name}[${result.command.pid}] Result:")
        sb.appendLine("Formatted value : ${result.formattedValue}" + " Value: ${result.value}" + " Raw response: ${result.rawResponse}")
        sb.appendLine("Unit: ${result.unit}")

        return sb.toString()
    }
}