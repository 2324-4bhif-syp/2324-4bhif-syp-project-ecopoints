package at.htl.ecopoints.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.activity.TripActivity
import androidx.compose.ui.Alignment
import at.htl.ecopoints.activity.ui.theme.ProfileActivity
import at.htl.ecopoints.activity.ui.theme.RankingActivity

@Composable
fun BottomNavBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    context: Context
) {
    val screens = listOf("Home", "Trip", "Ranking", "Profile")
    BottomNavigation(
        modifier = Modifier
            .padding(0.dp, 775.dp, 0.dp, 0.dp)
    ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    val icon = when (screen) {
                        "Home" -> Icons.Default.Home
                        "Trip" -> Icons.Default.List
                        "Ranking" -> Icons.Default.KeyboardArrowUp
                        "Profile" -> Icons.Default.AccountCircle
                        else -> Icons.Default.Home
                    }
                    Icon(icon, contentDescription = null)
                },
                selected = currentScreen == screen,
                onClick = {
                    if (currentScreen != screen) {
                        onScreenSelected(screen)
                        val intent = when (screen) {
                            "Home" -> Intent(context, MainActivity::class.java)
                            "Trip" -> Intent(context, TripActivity::class.java)
                            "Ranking" -> Intent(context, RankingActivity::class.java)
                            "Profile" -> Intent(context, ProfileActivity::class.java)
                            else -> Intent(context, MainActivity::class.java)
                        }
                        context.startActivity(intent)
                    }
                }
            )
        }
    }
}