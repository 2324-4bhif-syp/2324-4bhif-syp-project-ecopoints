package at.htl.ecopoints.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.MainActivity
import at.htl.ecopoints.activity.TripActivity

@Composable
fun BottomNavBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    context: Context
) {
    val screens = listOf("Home", "Trip")
    BottomNavigation(
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    val icon = when (screen) {
                        "Home" -> Icons.Default.Home
                        "Trip" -> Icons.Default.List
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
                            else -> Intent(context, MainActivity::class.java)
                        }
                        context.startActivity(intent)
                    }
                }
            )
        }
    }
}