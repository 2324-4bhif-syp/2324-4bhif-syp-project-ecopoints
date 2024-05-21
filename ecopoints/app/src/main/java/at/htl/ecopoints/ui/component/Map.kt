package at.htl.ecopoints.ui.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.model.PolylineNode
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline

@Composable
fun ShowMap(modifier: Modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            properties: MapProperties = MapProperties(mapType = MapType.HYBRID, isMyLocationEnabled = true),
            latLngList: List<PolylineNode> = listOf()){
    GoogleMap(
        modifier = modifier,
        properties = properties
    ) {
        if(latLngList.isNotEmpty())
            DrawPolyLine(latLngList);
    }
}

@Composable
fun DrawPolyLine(latLngList: List<PolylineNode> = listOf()) {
    for (i in 0 until latLngList.size - 1) {
        Polyline(
            points = listOf(
                LatLng(latLngList[i].latitude, latLngList[i].longitude),
                LatLng(latLngList[i + 1].latitude, latLngList[i + 1].longitude)
            ),
            color = Color(latLngList[i].color),
            width = 10f
        )
    }

    Log.d("DrawPolyLine", "Keck: ${latLngList.size}, ${Color(latLngList[latLngList.size - 1].color)}")
}