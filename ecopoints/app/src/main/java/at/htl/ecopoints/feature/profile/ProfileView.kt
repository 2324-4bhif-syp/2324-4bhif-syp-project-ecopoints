package at.htl.ecopoints.feature.profile

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAddAlt1
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.R
import at.htl.ecopoints.model.ProfileInfo
import at.htl.ecopoints.model.RankingInfo
import at.htl.ecopoints.model.Store
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

    @Composable
    fun Profile(){
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ProfileSettingsHeader()
                ProfileHeader()
                ShowStatistics()
                FriendSuggestions()
            }
        }
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
                        ShowStatistics()
                        FriendSuggestions()
                    }
                }
            }
        }
    }
    
    @Composable
    private fun FriendSuggestions(){
        val state = store.pipe.map { it.rankingInfo }.subscribeAsState(RankingInfo())

        Row(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 15.dp)
        ) {
            Column {
                Text(
                    text = "Friend Suggestions",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(25f, TextUnitType.Sp)
                )

                LazyRow {
                    items(state.value.users) {user ->
                        if(!user.userName.equals(state.value.currentUser.userName)) {
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(140.dp)
                                    .height(200.dp),
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.no_profile_pic),
                                        contentDescription = "Profile Picture",
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(80.dp)),
                                        alignment = Alignment.TopCenter
                                    )

                                    Text(
                                        text = user.userName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = TextUnit(18f, TextUnitType.Sp),
                                        modifier = Modifier.padding(top = 10.dp)
                                    )

                                    Text(
                                        text = "Follows you",
                                        fontSize = TextUnit(15f, TextUnitType.Sp),
                                        color = Color.Gray
                                    )

                                    Button(
                                        onClick = { /*TODO*/ },
                                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colorScheme.background),
                                        modifier = Modifier
                                            .padding(top = 10.dp),
                                        shape = RoundedCornerShape(30),
                                        border = BorderStroke(1.dp, Color.LightGray)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.PersonAddAlt1,
                                            contentDescription = "Add Friends",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @Composable
    private fun ShowStatistics(){
        val state = store.pipe.map { it.profileInfo }.subscribeAsState(ProfileInfo())

        val cardWidth = 175.dp
        val cardHeight = 80.dp
        val cardBorderWidth = 1.dp
        val cardBorderColor = MaterialTheme.colorScheme.primary
        val cardCornerShapeSize = 20.dp

        Row(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 15.dp)) {
            Column {
                androidx.compose.material.Text(
                    text = "Statistics",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(25f, TextUnitType.Sp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                )

                val row = state.value.currentUser.detailRankingCardContentList[0]
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (col in row) {
                        Column(modifier = Modifier.weight(1f)) {
                            Card(
                                border = BorderStroke(cardBorderWidth, cardBorderColor),
                                shape = RoundedCornerShape(cardCornerShapeSize),
                                modifier = Modifier
                                    .size(width = cardWidth, height = cardHeight)
                            ) {
                                Row {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Image(
                                            painter = painterResource(id = col.icon),
                                            contentDescription = col.description,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.padding(top = 10.dp)) {
                                        Row {
                                            androidx.compose.material.Text(
                                                text = col.value,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = TextUnit(20f, TextUnitType.Sp)
                                            )
                                        }

                                        Row {
                                            androidx.compose.material.Text(text = col.description)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ProfileSettingsHeader(){
        Row(
            modifier = Modifier
                .padding(end = 16.dp, start = 16.dp)
                .height(40.dp)
        ) {
            Column(
                modifier = Modifier.width(40.dp)
            ) {

            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
            ) {
                Row {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                }
            }

            Column(
                modifier = Modifier.width(40.dp)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Profile Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        androidx.compose.material3.Divider(thickness = 1.dp, color = Color.LightGray)
    }

    @Composable
    fun ProfileHeader(){
        val state = store.pipe.map { it.profileInfo }.subscribeAsState(ProfileInfo())

        Row(modifier = Modifier.padding(top = 10.dp,start = 16.dp, end = 16.dp, bottom = 10.dp)) {
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
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterVertically)
                    )

                    androidx.compose.material.Text(
                        text = "Joined February 2024",
                        modifier = Modifier.padding(start = 5.dp),
                        color = Color.Gray
                    )
                }

                Row(modifier = Modifier.padding(top = 12.dp)) {
                    Column{
                        Text(
                            text = "50 Following",
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(15f, TextUnitType.Sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = "100 Followers",
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(15f, TextUnitType.Sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
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

        Row {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colorScheme.background),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(30),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonAddAlt1,
                    contentDescription = "Add Friends",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "ADD FRIENDS",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 8.dp)
                )
            }
        }

        androidx.compose.material3.Divider(thickness = 1.dp, color = Color.LightGray)
    }
}