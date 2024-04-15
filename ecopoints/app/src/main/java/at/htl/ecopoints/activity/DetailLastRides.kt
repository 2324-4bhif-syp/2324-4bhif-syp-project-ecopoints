/*
package at.htl.ecopoints.activity

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.R
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.model.User
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LastRidesScreen(trips: List<Trip>, context: Context) {
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            ShowReturnBtn(context)
            ShowProfileHeader(trips[0])
            ShowTrips(trips = trips)
        }
    }
}


@Composable
private fun ShowReturnBtn(context: Context){
    IconButton(onClick = {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp),
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ShowProfileHeader(trip: Trip) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(trip.startDate)

    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formattedDate + " Uhr",
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(20f, TextUnitType.Sp)
            )

            Row(modifier = Modifier.padding(top = 8.dp)) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Rounded.Timelapse,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = trip.distance.toString() + " km",
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }

        Column {
            Image(
                painter = painterResource(id = R.drawable.ranking_category_cars),
                contentDescription = "Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(80.dp))
            )
        }
    }

    androidx.compose.material3.Divider(thickness = 1.dp, color = Color.LightGray)
}

@Composable
private fun ShowTrips(trips: List<Trip>) {
    Column {
        trips.forEach { trip ->
            Row {
                Text(text = trip.distance.toString())
                Text(text = trip.avgSpeed.toString())
            }
        }
    }
}*/
