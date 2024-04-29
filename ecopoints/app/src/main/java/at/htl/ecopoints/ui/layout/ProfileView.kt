package at.htl.ecopoints.ui.layout

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.R
import at.htl.ecopoints.RankingActivity
import at.htl.ecopoints.model.ProfileInfo
import at.htl.ecopoints.model.RankingInfo
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.model.User
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        ProfileSettingsHeader()
                        ProfileHeader()

                        val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Profile") }
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
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
    }

    @Composable
    private fun ProfileSettingsHeader(){
        Column {
            Text(
                text = "Profile",
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f)
            )

            IconButton(onClick = { /*TODO*/ }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Profile Settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    @Composable
    fun ProfileHeader(){
        val state = store.subject.map { it.profileInfo }.subscribeAsState(ProfileInfo())
        Log.i("ProfileView", state.value.currentUser.userName)

        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                androidx.compose.material.Text(
                    text = state.value.currentUser.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(28f, TextUnitType.Sp)
                )

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Timelapse,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterVertically)
                    )

                    androidx.compose.material.Text(
                        text = "Joined February 2024",
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }

            Column {
                Image(
                    painter = painterResource(id = R.drawable.no_profile_pic),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(80.dp))
                )
            }
        }

        androidx.compose.material3.Divider(thickness = 1.dp, color = Color.LightGray)
    }
}