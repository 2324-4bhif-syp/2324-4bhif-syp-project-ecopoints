package at.htl.ecopoints.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AccelerometerSensorService(
    private val onSensorChanged: (Float, Float, Float) -> Unit,
    private val onResetSensors: () -> Unit
) : SensorEventListener {
    var sensorXMax: String = ""
    var sensorYMax: String = ""
    var sensorZMax: String = ""

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            if (x > sensorXMax as Float) {
                sensorXMax = x.toString()
            }
            if (y > sensorYMax as Float) {
                sensorYMax = y.toString()
            }
            if (z > sensorZMax as Float) {
                sensorZMax = z.toString()
            }

            onSensorChanged(x, y, z)
        }
    }

    fun registerListener(sensorManager: SensorManager, accelerometerSensor: Sensor) {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener(sensorManager: SensorManager) {
        sensorManager.unregisterListener(this)
    }

    fun resetSensors() {
        onResetSensors()
        sensorXMax = ""
        sensorYMax = ""
        sensorZMax = ""
    }
}