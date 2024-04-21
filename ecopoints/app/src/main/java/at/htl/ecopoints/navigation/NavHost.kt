package at.htl.ecopoints.navigation

import android.content.Context
import android.content.Intent
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
import at.htl.ecopoints.RankingActivity
import at.htl.ecopoints.ui.layout.RankingView
import at.htl.ecopoints.ui.layout.TripView

@Composable
fun BottomNavBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    context: Context
) {
    val screens = listOf("Home", "Ranking")


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
                            "Ranking" -> Icons.Default.List
                            else -> Icons.Default.Home
                        }
                        Icon(icon, contentDescription = null, tint = androidx.compose.material3.MaterialTheme.colorScheme.primary)
                    },
                    selected = currentScreen == screen,
                    onClick = {
                        if (currentScreen != screen) {
                            onScreenSelected(screen)
                            val intent = when (screen) {
                                "Home" -> Intent(context, MainActivity::class.java)
                                "Ranking" -> Intent(context, RankingActivity::class.java)
                                else -> Intent(context, MainActivity::class.java)
                            }
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}