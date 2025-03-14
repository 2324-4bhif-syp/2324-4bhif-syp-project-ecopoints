package at.htl.ecopoints.ui.layout

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.R
import at.htl.ecopoints.db.DBHelper
import at.htl.ecopoints.io.JsonFileWriter
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.HomeInfo
import at.htl.ecopoints.model.PolylineNode
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.model.dto.TripMetaData
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.component.ShowMap
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.util.DurationParser
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


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
            val isDarkMode = store.subject.map { it.isDarkMode }.subscribeAsState(false)

            EcoPointsTheme(
                darkTheme = isDarkMode.value
            ) {
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
//                    ShowPhoto()
//                    ShowPrices()
                    ShowLogo()

                    ShowText()

                    LastTrips(activity)

//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(bottom = 60.dp),
//                        contentAlignment = Alignment.BottomCenter
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp),
//                            horizontalArrangement = Arrangement.SpaceEvenly,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Column {
//                                androidx.compose.material.Button(
//                                    onClick = { shareJsonFile(activity) },
//                                    colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
//                                        backgroundColor = MaterialTheme.colorScheme.background
//                                    ),
//                                    shape = RoundedCornerShape(30),
//                                    border = BorderStroke(1.dp, Color.LightGray)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Share,
//                                        contentDescription = "Share JSON",
//                                        tint = MaterialTheme.colorScheme.primary
//                                    )
//
//                                    Text(
//                                        text = "JSON",
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = TextUnit(15f, TextUnitType.Sp),
//                                        color = MaterialTheme.colorScheme.primary,
//                                        modifier = Modifier.padding(
//                                            start = 8.dp,
//                                            bottom = 4.dp,
//                                            top = 4.dp
//                                        )
//                                    )
//                                }
//                            }
//
//                            Column {
//                                androidx.compose.material.Button(
//                                    onClick = { shareLogFile(activity) },
//                                    colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
//                                        backgroundColor = MaterialTheme.colorScheme.background
//                                    ),
//                                    shape = RoundedCornerShape(30),
//                                    border = BorderStroke(1.dp, Color.LightGray)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Share,
//                                        contentDescription = "Share Log",
//                                        tint = MaterialTheme.colorScheme.primary
//                                    )
//
//                                    Text(
//                                        text = "Log",
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = TextUnit(15f, TextUnitType.Sp),
//                                        color = MaterialTheme.colorScheme.primary,
//                                        modifier = Modifier.padding(
//                                            start = 8.dp,
//                                            bottom = 4.dp,
//                                            top = 4.dp
//                                        )
//                                    )
//                                }
//                            }
//
//                            Column {
//                                androidx.compose.material.Button(
//                                    onClick = {
//                                        jsonFileWriter.clearFile()
//                                        jsonFileWriter.clearLog()
//                                        Toast
//                                            .makeText(activity, "File cleared", Toast.LENGTH_SHORT)
//                                            .show() },
//                                    colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
//                                        backgroundColor = MaterialTheme.colorScheme.background
//                                    ),
//                                    shape = RoundedCornerShape(30),
//                                    border = BorderStroke(1.dp, Color.LightGray)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.ClearAll,
//                                        contentDescription = "Clear File",
//                                        tint = MaterialTheme.colorScheme.primary
//                                    )
//
//                                    Text(
//                                        text = "Clear",
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = TextUnit(15f, TextUnitType.Sp),
//                                        color = MaterialTheme.colorScheme.primary,
//                                        modifier = Modifier.padding(
//                                            start = 8.dp,
//                                            bottom = 4.dp,
//                                            top = 4.dp
//                                        )
//                                    )
//                                }
//                            }
//                        }
//                    }

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

    private fun shareLogFile(context: Context) {
        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(jsonFileWriter.logFilePath)
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "application/json"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Log File"))
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
                    containerColor = Transparent,
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
    private fun HomeHeader(context: Context) {
        val state = store.subject.map { it.homeInfo }.subscribeAsState(HomeInfo())
        var ecopoints = 0.0


        state.value.trips.forEach { trip ->
            ecopoints += trip.ecoPointsMetaData.ecoPoints
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ecopoints.toString(),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.ranking_category_ecopoints),
                    contentDescription = "Eco-Points",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
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
    fun ShowLogo() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter = painterResource(id = R.drawable.app_icon)

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }

//    @Composable
//    fun ShowPrices() {
//        val dieselPrice = remember { mutableStateOf<Double?>(null) }
//        val e5Price = remember { mutableStateOf<Double?>(null) }
//        val errorMessage = remember { mutableStateOf<String?>(null) }
//
//        LaunchedEffect(Unit) {
//            val apiClient = TankerkoenigApiClient()
//            apiClient.getApiData(object : ApiCallback {
//                override fun onSuccess(gasData: GasData) {
//                    dieselPrice.value = gasData.diesel
//                    e5Price.value = gasData.e5
//                }
//
//                override fun onError(error: String) {
//                    errorMessage.value = error
//                }
//            })
//        }
//
//        if (dieselPrice.value != null && e5Price.value != null) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "Diesel: ${String.format("%.2f", dieselPrice.value)}€",
//                    style = MaterialTheme.typography.titleLarge,
//                    modifier = Modifier.padding(end = 20.dp)
//                )
//                Text(
//                    text = "Benzin: ${String.format("%.2f", e5Price.value)}€",
//                    style = MaterialTheme.typography.titleLarge
//                )
//            }
//        } else if (errorMessage.value != null) {
//            Text(
//                text = errorMessage.value ?: "Unbekannter Fehler",
//                modifier = Modifier.padding(1.dp),
//                color = Color.Red,
//                style = MaterialTheme.typography.titleMedium
//            )
//        } else {
//            Text(
//                text = "Tankpreise werden geladen...",
//                modifier = Modifier.padding(1.dp),
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    }

    @Composable
    fun ShowText() {
        Text(
            text = "Trips",
            fontSize = TextUnit(25f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp, 160.dp, 0.dp, 0.dp),
        )
    }


    @Composable
    fun LastTripsButton(
        trip: TripMetaData,
        onClickAction: () -> Unit
    ) {
        androidx.compose.material.Button(
            onClick = onClickAction,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                val formattedStartDate = Instant.parse(trip.startDate).atZone(ZoneId.of("UTC")).format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                val duration = DurationParser.parse(trip.duration)
                val formattedDuration = java.lang.String.format(
                    "%02d:%02d",
                    duration.toHoursPart(),
                    duration.toMinutesPart()
                )


                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "${formattedStartDate} Uhr",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Column {
                        Text(
                            text = "${String.format("%.2f", trip.distance)} km"
                        )
                    }

                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = "${trip.ecoPointsMetaData.ecoPoints}")
                    Image(
                        painter = painterResource(id = R.drawable.ranking_category_ecopoints),
                        contentDescription = "Eco-Points",
                        modifier = Modifier
                            .size(27.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }

    @Composable
    fun LastTrips(context: Context) {
        val state = store.subject.map { it.homeInfo }.subscribeAsState(HomeInfo())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(0.dp, 170.dp, 0.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(state.value.trips) { trip ->
                    LastTripsButton(trip = trip) {
                        store.next {
                            it.homeInfo.showDialog = true
                            it.homeInfo.selectedTrip = trip
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 10.dp, 5.dp, 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 50.dp, end = 50.dp)
                ) {
                    Row {
                        androidx.compose.material.Button(
                            onClick = {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            },
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = MaterialTheme.colorScheme.background
                            ),
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30),
                            border = BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "New Trip",
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Trip",
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(20f, TextUnitType.Sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            )
                        }
                    }
                }
            }

            if (state.value.showDetailedLastRidesPopup) {
                val lastRidesView = LastRidesView()
                lastRidesView.compose(activity = context as ComponentActivity, store)
            }

            if (state.value.showDialog) {
                ShowTripPopupDialog(
                    showDialog = state.value.showDialog,
                    selectedTrip = state.value.selectedTrip,
                    trips = state.value.trips,
                    context = context,
                    onCloseDialog = {
                        store.next {
                            it.homeInfo.showDialog = false
                            it.homeInfo.selectedTrip = null
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
        selectedTrip: TripMetaData,
        trips: List<TripMetaData>,
        context: Context,
        onCloseDialog: () -> Unit
    ) {
        if (showDialog) {
            val formattedStartDate = Instant.parse(selectedTrip.startDate).atZone(ZoneId.of("UTC")).format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            val formattedEndDate = Instant.parse(selectedTrip.endDate).atZone(ZoneId.of("UTC")).format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

            val smallMapCameraPosition = remember {
                mutableStateOf(
                    CameraPositionState(
                        CameraPosition(
                            LatLng(
                                selectedTrip.latLngList.last().item1,
                                selectedTrip.latLngList.last().item2
                            ),
                            12f,
                            0f,
                            0f
                        )
                    )
                )
            }

            var isFullScreen by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = {
                    onCloseDialog()
                },
                title = {
                    Text("Trip: $formattedStartDate")
                },
                text = {
                    Column {
                        if (selectedTrip != null) {
                            Text("End Date: $formattedEndDate")
                            Text("Distance: ${String.format("%.2f", selectedTrip.distance)} km")
                            Text("Average Speed: ${String.format("%.2f", selectedTrip.averageSpeedObd)} km/h")
                            Text("Average RPM: ${String.format("%.2f", selectedTrip.averageRpm)} rpm")
                        } else {
                            Text("Trip details not available.")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Box {
                            ShowMap(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth(),
                                latLngList = selectedTrip.latLngList.map { PolylineNode(it.item1, it.item2, 0.0) },
                                cameraPositionState = smallMapCameraPosition.value
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .clickable { isFullScreen = true }
                                    .padding(8.dp)
                            ) {
                                Text("Fullscreen", color = Color.Black)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onCloseDialog() }) {
                        Text("OK")
                    }
                }
            )

            if (isFullScreen) {
                Dialog(
                    onDismissRequest = { isFullScreen = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false) // Wichtig für echtes Fullscreen!
                ) {
                    val fullScreenCameraPosition = remember {
                        mutableStateOf(
                            CameraPositionState(
                                CameraPosition(
                                    smallMapCameraPosition.value.position.target,
                                    smallMapCameraPosition.value.position.zoom,
                                    smallMapCameraPosition.value.position.tilt,
                                    smallMapCameraPosition.value.position.bearing
                                )
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black) // Fix für richtige Bildschirmfüllung
                    ) {
                        ShowMap(
                            modifier = Modifier.fillMaxSize(),
                            latLngList = selectedTrip.latLngList.map { PolylineNode(it.item1, it.item2, 0.0) },
                            cameraPositionState = fullScreenCameraPosition.value
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .clickable { isFullScreen = false }
                                    .padding(8.dp)
                            ) {
                                Text("Close", color = Color.Black)
                            }
                        }
                    }
                }
            }
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

                val trip = Trip(
                    id,
                    carId,
                    userId,
                    distance,
                    avgSpeed,
                    avgEngineRotation,
                    startDate,
                    endDate,
                    rewardedEcoPoints
                )
                dbHelper.addTrip(trip)

                line = reader.readNext()
            }
        } catch (e: IOException) {
            Log.e("CSV-Error", "Fehler beim Lesen der CSV-Datei $fileName: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("CSV-Error", "Allgemeiner Fehler in readTripData2FromCsvAndAddToDB: ${e.message}", e)
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
                val tripId = UUID.fromString(line[1])
                val longitude = line[2].toDouble()
                val latitude = line[3].toDouble()
                val currentEngineRPM = line[4].toDouble()
                val currentVelocity = line[5].toDouble()
                val throttlePosition = line[6].toDouble()
                val engineRunTime = line[7]

                val carData = CarData(
                    id,
                    tripId,
                    longitude,
                    latitude,
                    currentEngineRPM,
                    currentVelocity,
                    throttlePosition,
                    engineRunTime,
                    timeStamp
                )

                dbHelper.addCarData(carData)

                counter++;

                if (counter == 4) {
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

    private fun addFakeDataToDB(context: Context) {
        val dbHelper = DBHelper(context, null)
        dbHelper.onUpgrade(dbHelper.writableDatabase, 1, 2)

        readTripData2FromCsvAndAddToDB("tripData.csv", context);
        readCarDataFromCsvAndAddToDB("carData.csv", context);

        dbHelper.close()
    }

    private fun getLatLngsFromTripDB(
        context: Context,
        tripId: UUID
    ): List<Pair<Color, Pair<LatLng, Double>>> {
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