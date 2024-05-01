package at.htl.ecopoints.model.reading;

import android.util.Log
import at.htl.ecopoints.model.Store
import com.github.eltonvs.obd.command.ObdResponse
import com.github.eltonvs.obd.command.engine.AbsoluteLoadCommand
import com.github.eltonvs.obd.command.engine.LoadCommand
import com.github.eltonvs.obd.command.engine.RPMCommand
import com.github.eltonvs.obd.command.engine.RelativeThrottlePositionCommand
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.command.engine.ThrottlePositionCommand
import com.github.eltonvs.obd.command.fuel.FuelConsumptionRateCommand
import com.github.eltonvs.obd.command.fuel.FuelTypeCommand
import com.github.eltonvs.obd.command.temperature.EngineCoolantTemperatureCommand
import com.github.eltonvs.obd.command.temperature.OilTemperatureCommand
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext


@Singleton
class ObdReaderKt {
    private val TAG = ObdReader::class.java.getSimpleName()

    @Inject
    lateinit var store: Store

    val scope = CoroutineScope(Dispatchers.IO)

    val obdCommands = listOf(
        RPMCommand(),
        SpeedCommand(),
        FuelConsumptionRateCommand(),
        LoadCommand(),
        AbsoluteLoadCommand(),
        ThrottlePositionCommand(),
        RelativeThrottlePositionCommand(),
        FuelTypeCommand(),
        EngineCoolantTemperatureCommand(),
        OilTemperatureCommand()
    )

    fun startReading(inputStream: InputStream?, outputStream: OutputStream?) {

        scope.launch {
            try {
                val obdConnection = ObdDeviceConnection(inputStream!!, outputStream!!)

                while (isActive) {
                    obdCommands.forEach { command ->
                        try {
                            val result = obdConnection.run(command, false, 0, 0)
                            Log.d(
                                TAG,
                                "${command.name}: $result"
                            )

                            delay(200)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error running OBD2 command ${command.name}", e)
                        }
                    }
                    delay(1000)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error while setting up OBD connection", e)
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
}