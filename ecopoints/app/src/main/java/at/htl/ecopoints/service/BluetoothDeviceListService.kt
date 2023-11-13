package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder

@Suppress("DEPRECATION")
class BluetoothDeviceListService : Service() {
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        BluetoothAdapter.getDefaultAdapter()
    }

    @SuppressLint("MissingPermission")
    fun getAllDevices(): List<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        bluetoothAdapter!!.cancelDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceByAddress(address: String): BluetoothDevice? {
        val devices = getAllDevices()
        var device: BluetoothDevice? = null
        for (it : BluetoothDevice in devices){
            if(it.address != null){
                if(it.address.toString() == address.toString()){
                    device = it
                }
            }
        }

        return device
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}