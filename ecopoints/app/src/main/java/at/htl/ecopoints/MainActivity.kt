package at.htl.ecopoints

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview


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
                    Preview()

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
    fun CustomCircularProgressIndicator(
        modifier: Modifier = Modifier,
        initialValue: Int,
        primaryColor: Color,
        secondaryColor: Color,
        minValue: Int = 0,
        maxValue: Int = 100,
        circleRadius: Float,
        onPositionChange:(Int)->Unit
    ){
        var circleCenter by remember {
            mutableStateOf(Offset.Zero)
        }

        var positionValue by remember {
            mutableStateOf(initialValue)
        }

        Box(
            modifier = modifier
        ){
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ){
                val width = size.width
                val height = size.height
                val circleThickness = width / 30f
                circleCenter = Offset(x = width/2f, y = height/2f)

                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(
                        )
                    ),

                    radius = circleRadius,
                    center = circleCenter
                )

                drawCircle(
                    style = Stroke(
                        width = circleThickness
                    ),
                    color = androidx.compose.ui.graphics.Color.Gray,
                    radius = circleRadius,
                    center = circleCenter
                )

                drawArc(
                    color = androidx.compose.ui.graphics.Color.Cyan,
                    startAngle = 90f,
                    sweepAngle = (360/maxValue) * positionValue.toFloat(),
                    style = Stroke(
                        width = circleThickness,
                        cap = StrokeCap.Round
                    ),
                    useCenter = false,
                    size = Size(
                        width = circleRadius * 2f,
                        height = circleRadius * 2f
                    ),
                    topLeft = Offset(
                        (width - circleRadius * 2f)/2f,
                        (height - circleRadius * 2f)/2f
                    )
                )


            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview(){
        CustomCircularProgressIndicator(
            modifier = Modifier
                .size(250.dp)
                .background(androidx.compose.ui.graphics.Color.DarkGray)
            ,
            initialValue = 67,
            primaryColor = Color.valueOf(105f,245f,140f,0.8f),
            secondaryColor = Color.valueOf(154f,156f,155f,0.8f),
            circleRadius = 230f,
            onPositionChange = {

            }
        )
    }
}
