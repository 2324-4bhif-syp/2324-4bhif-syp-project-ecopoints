package at.htl.ecopoints.navigations

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.ui.theme.EcoPointsTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoPointsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    startMainAct()
                }
            }
        }
    }
}

@Composable
private fun startMainAct(){
    val contex = LocalContext.current
    contex.startActivity(Intent(contex, MainActivity::class.java))

}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "HOME",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcoPointsTheme {
        Greeting("Android")
    }
}