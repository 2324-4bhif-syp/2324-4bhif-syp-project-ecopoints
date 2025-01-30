package at.htl.ecopoints.ui.layout

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.service.TripService
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import javax.inject.Inject
import javax.inject.Singleton


//private val TAG = MainView::class.java.simpleName

@Singleton
class MainView {

    @Inject
    lateinit var store: Store

    @Inject
    constructor() {
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CheckResult", "UnusedMaterial3ScaffoldPaddingParameter")
    fun compose(activity: ComponentActivity) {
        activity.setContent {
            val isDarkMode = store.subject.map { it.isDarkMode }.subscribeAsState(false)

            EcoPointsTheme(
                darkTheme = isDarkMode.value
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Text("Hello World!")
//                    BottomNavBar(currentScreen = this@MainView, onScreenSelected = {}, activity = this)
                }
            }
        }
    }
}