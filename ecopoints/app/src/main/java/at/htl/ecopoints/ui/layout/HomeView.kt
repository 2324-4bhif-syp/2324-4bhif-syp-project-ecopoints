package at.htl.ecopoints.ui.layout

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htl.ecopoints.R
import at.htl.ecopoints.db.DBHelper
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.HomeInfo
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.model.TankerkoenigApiClient
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.thread


@Singleton
class HomeView {

    @Inject
    lateinit var store: Store

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


                    ShowPrices()

                    ShowPhoto()
                    ShowText()

                    //LastTrips()

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
    }


    @Composable
    fun ShowPhoto() {

        val painter = painterResource(id = R.drawable.app_icon)

        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .scale(2.0f)
                    .padding(top = 30.dp)
            )
        }
    }

    @Composable
    fun ShowPrices() {
        var dieselPrice = 0.0;
        var e5Price = 0.0;

        val tankerkoenigApiClient = TankerkoenigApiClient()
        try {
            thread {
                val gasData = tankerkoenigApiClient.getApiData()
                dieselPrice = gasData.diesel
                e5Price = gasData.e5
            }

            while(dieselPrice == 0.0 && e5Price == 0.0) {
                Thread.sleep(0.1.toLong())
            }

        }catch (e: Exception) {
            Log.e("Tankpreis Error", "Error: ${e.message}")
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 25.sp, fontStyle = FontStyle.Italic)) {
                    append("Diesel\n")
                }
                append("  ${dieselPrice}€")
            },
            modifier = Modifier.padding(start = 80.dp, top = 150.dp),
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 25.sp, fontStyle = FontStyle.Italic)) {
                    append("Benzin\n")
                }
                append("  ${e5Price}€")
            },
            modifier = Modifier.padding(start = 260.dp, top = 150.dp),
        )

    }

    @Composable
    fun ShowText(){
        val gradientColors = listOf(Gray, Green, DarkGray)

        Text(
            text = "Last Rides:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(10.dp, 240.dp, 0.dp,0.dp),

            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
        )
    }


    private fun readTripDataFromCsvAndAddToDB(fileName: String, context: Context) {
        val dbHelper = DBHelper(context, null)

        dbHelper.onUpgrade(dbHelper.writableDatabase, 1, 2)

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

    private fun addFakeDataToDB(context: Context){
        val dbHelper = DBHelper(context, null)
        dbHelper.onUpgrade(dbHelper.writableDatabase, 1, 2)

        readTripData2FromCsvAndAddToDB("tripData.csv", context);
        readCarDataFromCsvAndAddToDB("carData.csv", context);

        dbHelper.close()
    }


}