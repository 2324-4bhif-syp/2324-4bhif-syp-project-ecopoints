package at.htl.ecopoints.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import at.htl.ecopoints.service.BluetoothDeviceListService

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

        };
    }

    @Composable
    /*fun GoogleMap(
        modifier: Modifier = Modifier;
    )*/
}