package at.htl.ecopoints.ui.layout

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.R
import at.htl.ecopoints.db.DBHelper
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.HomeInfo
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.apis.TankerkoenigApiClient
import at.htl.ecopoints.io.JsonFileWriter
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.component.ShowMap
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.google.android.gms.maps.model.LatLng
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Iterable
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.thread


@Singleton
class HomeView {

    @Inject
    lateinit var store: Store

    @Inject
    lateinit var jsonFileWriter: JsonFileWriter

    @Inject
    constructor() {
    }

    fun compose(activity: ComponentActivity) {
        activity.setContent {
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        HomeHeader(activity)

                    }
                    ShowPhoto()
                    ShowPrices()

                    ShowText()

                    LastTrips(activity)

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GradientButton(
                                onClick = { shareJsonFile(activity) },
                                text = "Share JSON File",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            )
                            GradientButton(
                                onClick = {
                                    jsonFileWriter.clearFile()
                                    Toast.makeText(activity, "File cleared", Toast.LENGTH_SHORT).show()
                                },
                                text = "Clear File",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            )
                        }
                    }

                    val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Home") }
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = activity
                        )
                    }
                }
            }
        }

//        writeDataToJsonFile()
    }

    private fun writeDataToJsonFile() {
        val jsonData = "{\"message\": \"Hello from HomeView\"}"
        jsonFileWriter.appendJson(jsonData)

        val filePath = jsonFileWriter.filePath
        Log.d("HomeView", "JSON-Datei gespeichert unter: $filePath")
    }


    private fun shareJsonFile(context: Context) {
        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(jsonFileWriter.filePath)
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "application/json"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share JSON File"))
    }

    @Composable
    fun GradientButton(
        onClick: () -> Unit,
        text: String,
        modifier: Modifier = Modifier
    ) {
        val gradientColors = listOf(Color(0xFF9bd99e), Color(0xFF05900a), Color(0xFF9bd99e))
        Box(
            modifier = modifier
                .height(50.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Black
                ),
                contentPadding = PaddingValues(),
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(text)
            }
        }
    }




    @Composable
    private fun HomeHeader(context: Context){

        val trips = getTripDataFromDB(context)
        var ecopoints = 0.0;


        trips.forEach{
                trip ->
            ecopoints += trip.rewardedEcoPoints
        }


        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.home_austria_flag),
                contentDescription = "flag",
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.home_crown),
                    contentDescription = "crown",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Text(
                    text = String.format("%.1f", ecopoints),
                    style = TextStyle(
                        color = Color(0xFFFFD700),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.home_friends),
                    contentDescription = "friends",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Text(
                    text = "6",
                    style = TextStyle(
                        color = Color(0xFF00A5FF),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }

        Divider(thickness = 1.dp, color = Color.LightGray)
    }

    @Composable
    fun ShowPhoto() {
        val painter = painterResource(id = R.drawable.app_icon)

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .scale(2.0f)
                    .padding(top = 60.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }


    @Composable
    fun ShowPrices() {
        var dieselPrice = 0.0
        var e5Price = 0.0

        val tankerkoenigApiClient = TankerkoenigApiClient()
        try {
            thread {
//                val gasData = //tankerkoenigApiClient.getApiData()
//                dieselPrice = gasData.diesel
                e5Price = 3.5// gasData.e5
            }

            while (dieselPrice == 0.0 && e5Price == 0.0) {
                Thread.sleep(0.1.toLong())
            }

        } catch (e: Exception) {
            Log.e("Tankpreis Error", "Error: ${e.message}")
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 25.sp, fontStyle = FontStyle.Italic)) {
                    append("Diesel\n")
                }
                append("  ${String.format("%.2f", dieselPrice)}€")
            },
            modifier = Modifier.padding(start = 90.dp, top = 180.dp),
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 25.sp, fontStyle = FontStyle.Italic)) {
                    append("Benzin\n")
                }
                append("  ${String.format("%.2f", e5Price)}€")
            },
            modifier = Modifier.padding(start = 270.dp, top = 180.dp),
        )
    }


    @Composable
    fun ShowText(){
        val gradientColors = listOf(Gray, Green, DarkGray)

        Text(
            text = "Top 3 Rides:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(10.dp, 270.dp, 0.dp,0.dp),

            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
        )
    }

    @Composable
    fun LastTripsButton(
        trip: Trip,
        onClickAction: () -> Unit
    ) {
        val gradientColors = listOf(Gray, Green, DarkGray)

        Button(
            onClick = onClickAction,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Transparent,
                contentColor = Black
            )
        ) {
            val formattedDate = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(trip.start)
            Column(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
            ) {
                Row() {
                    Text(formattedDate + " Uhr")
                }
                Row {
                    Text(trip.rewardedEcoPoints.toString() + " EP")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = trip.distance.toString() + " km")
                }
            }
        }
    }


    @Composable
    fun LastTrips(context: Context) {

        val state = store.subject.map { it.homeInfo }.subscribeAsState(HomeInfo())

        val gradientColors = listOf(
            Color(0xFF9bd99e),
            Color(0xFF05900a),
            Color(0xFF9bd99e)
        )

//        addFakeDataToDB(context) infinite call loop

        val trips = getTopThreeTripDataFromDB(context)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(0.dp, 290.dp, 0.dp, 50.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(trips) { trip ->
                    LastTripsButton(trip = trip) {
                        store.next {
                            it.homeInfo.showDialog = true
                            it.homeInfo.selectedTripDate = trip.start
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 10.dp, 30.dp, 220.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        store.next {
                            it.homeInfo.showDetailedLastRidesPopup = true
                        }
                    },
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = gradientColors
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .weight(1f)
                        .padding(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Transparent,
                        contentColor = Black
                    )
                ) {
                    Text("View All")
                }

                Spacer(modifier = Modifier.width(30.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = gradientColors
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .weight(1f)
                        .padding(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Transparent,
                        contentColor = Black
                    )
                ) {
                    Text("New Trip")
                }
            }

            if (state.value.showDetailedLastRidesPopup) {
                val lastRidesView = LastRidesView()
                lastRidesView.compose(activity = context as ComponentActivity, store)
            }

            if (state.value.showDialog) {
                ShowTripPopupDialog(
                    showDialog = state.value.showDialog,
                    selectedTripDate = state.value.selectedTripDate,
                    trips = trips,
                    context = context,
                    onCloseDialog = {
                        store.next {
                            it.homeInfo.showDialog = false
                            it.homeInfo.selectedTripDate = null
                            it.homeInfo.showDetailedLastRidesPopup = false
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun ShowTripPopupDialog(
        showDialog: Boolean,
        selectedTripDate: Date?,
        trips: List<Trip>,
        context: Context,
        onCloseDialog: () -> Unit
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    onCloseDialog()
                },
                title = {
                    Text(
                        "Trip: " + (selectedTripDate?.let {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                        } ?: "Unknown Date")
                    )
                },
                text = {
                    Column {
                        val selectedTrip = trips.find { it.start == selectedTripDate }
                        if (selectedTrip != null) {
                            Text("End Date: ${SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(selectedTrip.end)}")
                            Text("Distance: ${selectedTrip.distance} km")
                            Text("Average Speed: ${selectedTrip.avgSpeed} km/h")
                            Text("Average Engine Rotation: ${selectedTrip.avgEngineRotation} rpm")
                            Text("Eco Points: ${selectedTrip.rewardedEcoPoints}")
                        } else {
                            Text("Trip details not available.")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ShowMap(
//                            cameraPositionState = rememberCameraPositionState {
//                                position = CameraPosition.fromLatLngZoom(
//                                    LatLng(getLatLngsFromTripDB(context, selectedTrip!!.id)
//                                        .first().second.first.latitude,
//                                        getLatLngsFromTripDB(context, selectedTrip!!.id)
//                                            .first().second.first.longitude), 10f)
//                            },
                            //latLngList = getLatLngsFromTripDB(context, selectedTrip!!.id)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        onCloseDialog()
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }



    private fun readTripData2FromCsvAndAddToDB(fileName: String, context: Context) {
        val dbHelper = DBHelper(context, null)

        try {
            val inputStream: InputStream = context.assets.open(fileName)
            val reader = CSVReaderBuilder(InputStreamReader(inputStream))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .build()

            val header = reader.readNext()

            var line = reader.readNext()
            while (line != null) {
                val id = UUID.fromString(line[0])
                val carId = line[1].toLong()
                val userId = line[2].toLong()
                val distance = line[3].toDouble()
                val avgSpeed = line[4].toDouble()
                val avgEngineRotation = line[5].toDouble()
                val startDate = Date(line[6].toLong())
                val endDate = Date(line[7].toLong())
                val rewardedEcoPoints = line[8].toDouble()

                val trip = Trip(id, carId, userId, distance, avgSpeed, avgEngineRotation, startDate, endDate, rewardedEcoPoints)
                dbHelper.addTrip(trip)

                line = reader.readNext()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            dbHelper.close()
        }
    }

    private fun readCarDataFromCsvAndAddToDB(fileName: String, context: Context) {
        val dbHelper = DBHelper(context, null)

        var counter = 0;

        try {
            val inputStream: InputStream = context.assets.open(fileName)
            val reader = CSVReaderBuilder(InputStreamReader(inputStream))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .build()

            val header = reader.readNext()

            var line = reader.readNext()
            while (line != null) {

                val dateString = line[8]
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = dateFormat.parse(dateString)
                val timeStamp = Timestamp(date.time)

                val id = line[0].toLong()
                val tripId =  UUID.fromString(line[1])
                val longitude =  line[2].toDouble()
                val latitude =  line[3].toDouble()
                val currentEngineRPM =  line[4].toDouble()
                val currentVelocity =  line[5].toDouble()
                val throttlePosition =  line[6].toDouble()
                val engineRunTime =  line[7]

                val carData = CarData(
                    id, tripId, longitude, latitude, currentEngineRPM, currentVelocity, throttlePosition, engineRunTime, timeStamp
                )

                dbHelper.addCarData(carData)

                counter++;

                if(counter == 4) {
                    dbHelper.updateTripValues(UUID.fromString(line[1]))
                    counter = 0;
                }

                line = reader.readNext()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            dbHelper.close()
        }
    }


    private fun getTripDataFromDB(context: Context): List<Trip> {
        val dbHelper = DBHelper(context, null)
        val trips = dbHelper.getAllTrips()
        dbHelper.close()
        return trips
    }

    private fun getTopThreeTripDataFromDB(context: Context): List<Trip> {
        val dbHelper = DBHelper(context, null)
        val trips = dbHelper.getTopThreeTrips()
        dbHelper.close()
        return trips
    }

    private fun addFakeDataToDB(context: Context){
        val dbHelper = DBHelper(context, null)
        dbHelper.onUpgrade(dbHelper.writableDatabase, 1, 2)

        readTripData2FromCsvAndAddToDB("tripData.csv", context);
        readCarDataFromCsvAndAddToDB("carData.csv", context);

        dbHelper.close()
    }

private fun getLatLngsFromTripDB(context: Context,  tripId : UUID): List<Pair<Color, Pair<LatLng, Double>>> {
    val dbHelper = DBHelper(context, null)
    val data = dbHelper.getAllCarData()
    val latLngs = mutableStateListOf<Pair<Color, Pair<LatLng, Double>>>()

    //for testing purposes, change to fuel consumption when finished
    //TODO: change to fuel consumption

    for (d in data) {
        if (d.tripId == tripId) {
            if (d.currentEngineRPM <= 1500)
                latLngs.add(
                    Pair(
                        Color.Green,
                        Pair(LatLng(d.latitude, d.longitude), d.currentEngineRPM)
                    )
                )
            else if (d.currentEngineRPM > 1500 && d.currentEngineRPM <= 2500)
                latLngs.add(
                    Pair(
                        Color.Yellow,
                        Pair(LatLng(d.latitude, d.longitude), d.currentEngineRPM)
                    )
                )
            else if (d.currentEngineRPM > 2500 && d.currentEngineRPM <= 3500)
                latLngs.add(
                    Pair(
                        Color.Red,
                        Pair(LatLng(d.latitude, d.longitude), d.currentEngineRPM)
                    )
                )
            else
                latLngs.add(
                    Pair(
                        Color.Black,
                        Pair(LatLng(d.latitude, d.longitude), d.currentEngineRPM)
                    )
                )
        }
    }

    dbHelper.close()
    if (latLngs.isEmpty())
        latLngs.add(Pair(Black, Pair(LatLng(0.0, 0.0), 0.0)))
    return latLngs
}
}