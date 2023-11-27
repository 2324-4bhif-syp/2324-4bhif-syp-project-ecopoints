package at.htl.ecopoints.navigations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import at.htl.ecopoints.R
import at.htl.ecopoints.ui.theme.EcoPointsTheme

class TripActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoPointsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android")
                    showNavigatonButtons()
                }
            }
        }
    }
}


@Composable
private fun showNavigatonButtons() {
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 56.dp))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationButton(
                text = "Home",
                imageRedId = R.drawable.ic_home,
                onClick = {
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent);
                }
            )
            NavigationButton(
                text = "Trip",
                imageRedId = R.drawable.ic_trip,
                onClick = {
                    context.startActivity(Intent(context, TripActivity::class.java))
                }
            )
            NavigationButton(
                text = "Ranking",
                imageRedId = R.drawable.ic_ranking,
                onClick = {
                    context.startActivity(Intent(context, RankingActivity::class.java))
                }
            )
            NavigationButton(
                text = "Profile",
                imageRedId = R.drawable.ic_profile,
                onClick = {
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                }
            )
        }
    }
}

@Composable
private fun NavigationButton(text: String, imageRedId: Int ,onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(0.dp, 680.dp, 0.dp, 0.dp)
            .background(Color.LightGray),
        colors = ButtonDefaults.textButtonColors(contentColor = Color.Black),

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageRedId),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(text = text)
        }
    }
}



@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "TRIP",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    EcoPointsTheme {
        Greeting2("Android")
    }
}