import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.ui.layout.MainView
import at.htl.ecopoints.ui.layout.TripView
import javax.inject.Inject

@Inject
lateinit var mainView: MainView

@Inject
lateinit var tripView: TripView

@Composable
fun BottomNavBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    activity: ComponentActivity
) {


    val screens = listOf("Home", "Trip", "Ranking", "Profile")


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            screens.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        val icon = when (screen) {
                            "Home" -> Icons.Default.Home
                            "Trip" -> Icons.Default.Place
                            "Ranking" -> Icons.Default.List
                            "Profile" -> Icons.Default.AccountCircle
                            else -> Icons.Default.Home
                        }
                        Icon(icon, contentDescription = null, tint = androidx.compose.material3.MaterialTheme.colorScheme.primary)
                    },
                    selected = currentScreen == screen,
                    onClick = {
                        if (currentScreen != screen) {
                            onScreenSelected(screen)
                            when (screen) {
                                "Home" -> mainView.compose(activity)
                                "Trip" -> tripView.compose(activity)
//                                "Ranking" -> Intent(context, RankingActivity::class.java)
//                                "Profile" -> Intent(context, ProfileActivity::class.java)
                                else -> mainView.compose(activity)
                            }
                        }
                    }
                )
            }
        }
    }
}