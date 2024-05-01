package at.htl.ecopoints.ui.layout

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.HomeActivity
import at.htl.ecopoints.R
import at.htl.ecopoints.db.DBHelper
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Singleton
class LastRidesView {

//    @Inject
//    lateinit var store: Store
//
//    @Inject
//    constructor() {
//    }

    fun compose(activity: ComponentActivity, store: Store) {
        activity.setContent {
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ShowReturnBtn(activity, store)
                        ShowHeader(activity)
                        ShowTripStatistic(activity);

                        val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Home") }

                        Box {
                            BottomNavBar(
                                currentScreen = currentScreen,
                                onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                                context = activity
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowReturnBtn(context: Context, store: Store){

        IconButton(onClick = {

            store.next {
                it.homeInfo.showDetailedLastRidesPopup = false
            }

            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)


        }) {
            androidx.compose.material3.Icon(
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
    private fun ShowHeader(context: Context) {

        val dbHelper = DBHelper(context, null)
        val trips = dbHelper.getAllTrips()
        dbHelper.close()

        var sum = 0.0;

        trips.forEach{
                trip ->
            sum += trip.distance
        }

        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 50.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                androidx.compose.material.Text(
                    text = "Last Rides",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(40f, TextUnitType.Sp)
                )

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Timelapse,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterVertically)
                    )

                    androidx.compose.material.Text(
                        text = "%.1f".format(sum) + " km",
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
    }
    @Composable
    private fun ShowTripStatistic(context: Context) {

        val cardWidth = 175.dp
        val cardHeight = 80.dp
        val cardBorderWidth = 1.dp
        val cardBorderColor = MaterialTheme.colorScheme.primary
        val cardCornerShapeSize = 20.dp

        val dbHelper = DBHelper(context, null)
        val trips = dbHelper.getAllTrips()
        dbHelper.close()

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            trips.forEach { trip ->
                val formattedDate = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(trip.start)

                Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {

                    Text(
                        text = formattedDate + " Uhr",
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    trip.detailTripCardContentList.forEach { row ->
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            row.forEach { col ->
                                Card(
                                    border = BorderStroke(cardBorderWidth, cardBorderColor),
                                    shape = RoundedCornerShape(cardCornerShapeSize),
                                    modifier = Modifier
                                        .size(width = cardWidth, height = cardHeight)
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = col.icon),
                                            contentDescription = col.description,
                                            modifier = Modifier.size(30.dp)
                                        )

                                        Column(
                                            modifier = Modifier.padding(start = 10.dp)
                                        ) {
                                            Text(
                                                text = col.value,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = TextUnit(20f, TextUnitType.Sp)
                                            )
                                            Text(
                                                text = col.description
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    androidx.compose.material3.Divider(thickness = 1.dp, color = Color.LightGray)

                }
            }
        }
    }


}