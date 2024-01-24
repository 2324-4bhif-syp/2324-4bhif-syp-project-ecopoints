package at.htl.ecopoints

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.marginTop
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.model.Trip
import at.htl.ecopoints.service.TripAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


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
                    ShowPhoto()
                    Spacer(modifier = Modifier.height(20.dp))

                    Spacer(modifier = Modifier.height(20.dp))


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
                    .padding(top = 25.dp)
            )
        }

    }

    @Composable
    fun ShowTrips(context: Context, activity: Activity) {
        val listView = ListView(context)
        val trips: Array<Trip> = arrayOf(
            Trip(
                id = UUID.randomUUID(),
                distance = 100.0,
                avgSpeed = 60.0,
                avgEngineRotation = 1500.0,
                date = Date(System.currentTimeMillis() + 26300060),
                rewardedEcoPoints = 10.0
            ),
            Trip(
                id = UUID.randomUUID(),
                distance = 75.5,
                avgSpeed = 50.0,
                avgEngineRotation = 1200.0,
                date = Date(System.currentTimeMillis() - 56400000),
                rewardedEcoPoints = 8.0
            )

        )

        listView.setPadding(0, 500, 0, 0)
        listView.layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        listView.adapter = TripAdapter(activity, trips)
        listView.divider = null
        listView.isVerticalScrollBarEnabled = true
        val dialog: Dialog = Dialog(context)

        listView.isClickable = true
        listView.setOnItemClickListener { parent, view, position, id ->
            dialog.findViewById<TextView>(R.id.tripDate).text = trips[position].date.toString()
            dialog.findViewById<TextView>(R.id.distance).text = trips[position].distance.toString()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.Transparent.hashCode()))
            dialog.show()
        }

        this.addContentView(listView,
            ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
        )
    }

}
