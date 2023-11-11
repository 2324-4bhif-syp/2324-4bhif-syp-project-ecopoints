package at.htl.ecopoints.service

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.UUID

class BluetoothDeviceService : Service() {
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        BluetoothAdapter.getDefaultAdapter()
    }

    fun getAllDevices(): List<BluetoothDevice> {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return emptyList();
        }
        else{
            return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceByUUID(id : UUID) : BluetoothDevice{
        return getAllDevices().filter { it.uuids.get(0).equals(id) }.get(0)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}