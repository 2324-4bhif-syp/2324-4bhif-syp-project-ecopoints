package at.htl.ecopoints.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Polyline

@Composable
fun ShowMap(cameraPositionState: CameraPositionState = CameraPositionState(),
            modifier: Modifier = Modifier.fillMaxWidth().height(200.dp),
            properties: MapProperties = MapProperties(),
            latLngList: List<Pair<Color, Pair<LatLng, Double>>> = listOf()){
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier,
        properties = properties
    ) {
        if(latLngList.isNotEmpty())
            DrawPolyLine(latLngList);
    }
}

@Composable
fun DrawPolyLine(latLngList: List<Pair<Color, Pair<LatLng, Double>>> = listOf()) {
    for (i in 0 until latLngList.size - 1) {
        Polyline(
            points = listOf(
                latLngList[i].second.first,
                latLngList[i + 1].second.first
            ),
            color = latLngList[i].first,
            width = 10f
        )
    }
}