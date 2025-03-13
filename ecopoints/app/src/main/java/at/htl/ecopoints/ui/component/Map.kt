package at.htl.ecopoints.ui.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.model.PolylineNode
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline
import kotlin.compareTo

@Composable
fun ShowMap(modifier: Modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            properties: MapProperties = MapProperties(mapType = MapType.HYBRID, isMyLocationEnabled = true),
            latLngList: List<PolylineNode> = listOf(),
            draw: Boolean = false){
    GoogleMap(
        modifier = modifier,
        properties = properties,
        cameraPositionState = CameraPositionState(CameraPosition(LatLng(latLngList.last().latitude,
            latLngList.last().longitude), 12f, 0f, 0f)),
    ) {
        if(latLngList.isNotEmpty())
            DrawPolyLine(latLngList, draw);
    }
}

@Composable
fun DrawPolyLine(latLngList: List<PolylineNode> = listOf(), draw : Boolean){
    for (i in 0 until latLngList.size - 1) {
        Polyline(
            points = listOf(
                LatLng(latLngList[i].latitude, latLngList[i].longitude),
                LatLng(latLngList[i + 1].latitude, latLngList[i + 1].longitude)
            ),
            color =  when {
                latLngList[i].fuelCons <= 6.0 -> Color.Green
                latLngList[i].fuelCons > 6.0 && latLngList[i].fuelCons <= 12 -> Color.Yellow
                latLngList[i].fuelCons > 12 && latLngList[i].fuelCons <= 20 -> Color.Red
            else -> Color.Black
            },
            width = 10f
        )
    }

    Log.d("DrawPolyLine", "Info: ${latLngList.size}, ${latLngList[0].latitude}, ${latLngList[0].longitude}, ${latLngList[0].fuelCons}")
}