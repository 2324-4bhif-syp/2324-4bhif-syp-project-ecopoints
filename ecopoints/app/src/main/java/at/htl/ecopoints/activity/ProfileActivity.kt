package at.htl.ecopoints.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.R
import at.htl.ecopoints.activity.ui.theme.EcoPointsTheme
import at.htl.ecopoints.navigation.BottomNavBar

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Profile") }
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowPhoto()
                    ShowFields()
                    Box {
                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@ProfileActivity
                        )
                    }
                    ShowPoints(points = 68)
                }
            }

        }
    }

    @Composable
    fun ShowFields() {
        var name by remember { mutableStateOf("Abdullah Aldesoky") }
        var email by remember { mutableStateOf("abdulaldesoky@gmail.com") }
        var number by remember { mutableStateOf("06649188653") }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NameTextField(name = name) { newName ->
                name = newName
            }
            EmailTextField(email = email) { newEmail ->
                email = newEmail
            }
            NumberTextField(number = number) { newNumber ->
                number = newNumber
            }
            SaveButton {
                Toast.makeText(this@ProfileActivity, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun ShowPhoto() {
        val painter = painterResource(id = R.drawable.no_profile_pic)

        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .scale(0.40f)
            )
        }

    }

    @Composable
    fun NameTextField(name: String, onNameChanged: (String) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            TextField(
                value = name,
                onValueChange = { onNameChanged(it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            )
        }
    }

    @Composable
    fun EmailTextField(email: String, onEmailChanged: (String) -> Unit) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            TextField(
                value = email,
                onValueChange = { onEmailChanged(it) },
                label = { Text("E-Mail") },
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            )
        }
    }

    @Composable
    fun NumberTextField(number: String, onNumberChanged: (String) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            TextField(
                value = number,
                onValueChange = { onNumberChanged(it) },
                label = { Text("Number") },
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            )
        }
    }

    @Composable
    fun ShowPoints(points: Int) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "E.Points: $points",
                modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            )
        }
    }

    @Composable
    fun SaveButton(onSaveClicked: () -> Unit) {
        Button(
            onClick = onSaveClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 100.dp, end = 100.dp, top = 15.dp)
        ) {
            Text("Save")
        }

    }
}
