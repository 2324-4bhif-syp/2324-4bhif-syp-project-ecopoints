package at.htl.ecopoints

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.marginTop
import androidx.lifecycle.lifecycleScope
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.service.TankerkoenigApiClient
import at.htl.ecopoints.service.TripAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowPrices()

                    ShowPhoto()
                    ShowText()
                    ShowTrips(context = this, activity = this@MainActivity)

                    val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Home") }
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){

                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@MainActivity
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
                    .scale(3.0f)
                    .padding(top = 50.dp)
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

            }

        }catch (e: Exception) {
            Log.e("Tankpreis Error", "Error: ${e.message}")
        }

        Log.i("Tankpreis", "Diesel: ${dieselPrice}")
        Log.i("Tankpreis", "E5: ${e5Price}")

    }



    @Composable
    fun ShowText(){
        val gradientColors = listOf(Gray, Green, DarkGray)

        Text(
            text = "Last Rides:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(10.dp, 260.dp, 0.dp,0.dp),

            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
        )
    }

    @Composable
    fun ShowTrips(context: Context, activity: Activity) {
        val listView = ListView(context)
        val trips: Array<Trip> = arrayOf(
            Trip(
                id = UUID.randomUUID(),
                distance = 96.3,
                avgSpeed = 60.0,
                avgEngineRotation = 1500.0,
                date = Date(System.currentTimeMillis() + 26300060),
                rewardedEcoPoints = 10.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 75.4,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 56400000),
                rewardedEcoPoints = 8.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 60.2,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 66400000),
                rewardedEcoPoints = 8.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 12.5,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 176400000),
                rewardedEcoPoints = 8.0
            )

        )

        listView.setPadding(0, 800, 0, 0)
        listView.layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        listView.adapter = TripAdapter(activity, trips)
        listView.divider = null
        listView.isVerticalScrollBarEnabled = true

        this.addContentView(listView,
            ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
        )
    }

}
