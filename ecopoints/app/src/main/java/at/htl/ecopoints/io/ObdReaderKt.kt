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
import com.github.eltonvs.obd.command.at.SetSpacesCommand
import com.github.eltonvs.obd.command.bytesToInt
import com.github.eltonvs.obd.command.engine.RPMCommand
import com.github.eltonvs.obd.command.engine.RuntimeCommand
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.command.engine.ThrottlePositionCommand
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.sql.Timestamp
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ObdReaderKt {
    private val TAG = ObdReader::class.java.getSimpleName()

    @Inject
    lateinit var store: Store

    @Inject
    constructor() {

    }

    class RpmCleanedResCommand : ObdCommand() {
        override val tag = "ENGINE_RPM_CLEANED_RES"
        override val name = "Engine RPM_CLEANED_RES"
        override val mode = "01"
        override val pid = "0C"

        override val defaultUnit = "RPM"
        override val handler = { it: ObdRawResponse ->
            (bytesToInt(
                it.bufferedValue,
                bytesToProcess = 2
            ) / 4).toString()
        }
    }

    val scope = CoroutineScope(Dispatchers.IO)

    val obdCommands = listOf(
        RPMCommand(),
        RpmCleanedResCommand(),
//        SpeedCommand(),
//        FuelConsumptionRateCommand(),
//        LoadCommand(),
//        AbsoluteLoadCommand(),
//        ThrottlePositionCommand(),
//        RelativeThrottlePositionCommand(),
//        FuelTypeCommand(),
//        EngineCoolantTemperatureCommand(),
//        OilTemperatureCommand()
    )

    suspend fun setupELM(obdConnection: ObdDeviceConnection) {
        try {

        } catch (e: Exception) {
            obdConnection.run(ResetAdapterCommand())
            delay(200)
            obdConnection.run(SetEchoCommand(Switcher.OFF))
            delay(200)
            obdConnection.run(SetSpacesCommand(Switcher.OFF))
            delay(200)
            obdConnection.run(SelectProtocolCommand(ObdProtocols.AUTO))
            delay(200)
        }
    }

    fun startReading(inputStream: InputStream?, outputStream: OutputStream?) {
        scope.launch {
            try {
                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)

                setupELM(obdConnection)

                while (isActive) {
                    obdCommands.forEach { command ->
                        try {
                            val result = obdConnection.run(command, false, 0, 0)
                            var sb = StringBuilder()

                            sb.appendLine("${command.name} Result:")
                            sb.appendLine("PID: $command.pid")
                            sb.appendLine("Raw command: ${command.rawCommand}")
                            sb.appendLine("Formatted value value: ${result.formattedValue}")
                            sb.appendLine("Value: ${result.value}")
                            sb.appendLine("Raw response: ${result.rawResponse}")
                            sb.appendLine("Unit: ${result.unit}")

                            val logMsg = sb.toString()

                            Log.d(
                                TAG,
                                logMsg
                            )

                            delay(250)
                        } catch (e: Exception) {
                            //Log.e(TAG, "Error running OBD2 command ${command.name}", e)
                        }
                    }
                    delay(1000)
                }
            } catch (e: Exception) {
                //Log.e(TAG, "Error while setting up OBD connection", e)
            }
        }


//        //Setup
//        singleThreadExecutor.execute {
//            try {
//                val obdConnection = ObdDeviceConnection(
//                    inputStream!!,
//                    outputStream!!
//                )
//
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error while setting up OBD connection", e)
//            }
//        }
//
//
//        executor.scheduleAtFixedRate({
//            try {
//                val obdConnection = ObdDeviceConnection(
//                    inputStream!!,
//                    outputStream!!
//                )
//
//                val res: Any = obdConnection.run(RPMCommand(), false, 0, 0)
//                Log.d(TAG, "RPM: $res")
//            } catch (e: Exception) {
//                Log.e(TAG, "Error while setting up OBD connection", e)
//            }
//        }, 1, 300, TimeUnit.MILLISECONDS)
    }

    fun updateCarData(command: ObdCommand, result: ObdResponse) {
        store.next {

            try {

                when (command) {
                    is RPMCommand -> it.tripViewModel.carData.currentEngineRPM =
                        result.value.toDouble()

                    is SpeedCommand -> it.tripViewModel.carData.speed = result.value.toDouble()
                    is ThrottlePositionCommand -> it.tripViewModel.carData.throttlePosition =
                        result.value.toDouble()

                    is RuntimeCommand -> it.tripViewModel.carData.engineRunTime =
                        result.formattedValue
                }
            }
            catch (_: Exception) {
            }
            it.tripViewModel.carData.timeStamp = Timestamp(System.currentTimeMillis())

        }
    }
}