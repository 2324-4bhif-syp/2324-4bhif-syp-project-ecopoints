package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import java.util.UUID

@Suppress("DEPRECATION")
class BluetoothDeviceService : Service() {
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        BluetoothAdapter.getDefaultAdapter()
    }

    @SuppressLint("MissingPermission")
    fun getAllDevices(): List<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceByUUID(id: UUID): BluetoothDevice {
        return getAllDevices().filter { it.uuids.get(0).equals(id) }.get(0)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}