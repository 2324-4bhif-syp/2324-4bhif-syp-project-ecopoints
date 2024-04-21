package at.htl.ecopoints.ui.layout

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileView {

    @Inject
    lateinit var store: Store

    @Inject
    constructor() {
    }

    fun compose(activity: ComponentActivity) {
        activity.setContent {
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Text("Hello Profile!")
                }

                val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Profile") }
                Box(
                    modifier = Modifier.fillMaxSize()
                ){

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