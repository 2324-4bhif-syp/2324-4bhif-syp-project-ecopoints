package at.htl.ecopoints.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.model.User
import at.htl.ecopoints.R
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import at.htl.ecopoints.RankingActivity
import at.htl.ecopoints.model.Store

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileScreen(user: User, context: Context, store: Store, currentUser: User) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface(color = colorScheme.background) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    ShowReturnBtn(context, store)

                    ShowProfileHeader(user, currentUser)

                    ShowStatistics(user)
                }
            }
        }
    }
}

@Composable
private fun ShowReturnBtn(context: Context, store: Store){
    IconButton(onClick = {
        val intent = Intent(context, RankingActivity::class.java)
        context.startActivity(intent)

        store.next{
            it.rankingInfo.selectedUser = User()
            it.rankingInfo.showDetailRankingView = false
        }
    }) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp),
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            tint = colorScheme.primary
        )
    }
}

@Composable
private fun ShowStatistics(user: User){
    val cardWidth = 175.dp
    val cardHeight = 80.dp
    val cardBorderWidth = 1.dp
    val cardBorderColor = colorScheme.primary
    val cardCornerShapeSize = 20.dp

    Row(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 15.dp)) {
        Column {
            Text(
                text = "Statistics",
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(28f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            for(row in user.detailRankingCardContentList){
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for(col in row){
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
                                            Text(
                                                text = col.value,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = TextUnit(20f, TextUnitType.Sp)
                                            )
                                        }

                                        Row {
                                            Text(text = col.description)
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
}

@Composable
private fun ShowProfileHeader(user: User, currentUser: User) {
    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.userName,
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

                Text(
                    text = "Joined February 2024",
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
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

    if(!currentUser.userName.equals(user.userName)){
        Row {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = colorScheme.background),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(30),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonAddAlt1,
                    contentDescription = "Follow",
                    tint = colorScheme.primary
                )

                Text(
                    text = "FOLLOW",
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    color = colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 8.dp)
                )
            }
        }
    }

    Divider(thickness = 1.dp, color = Color.LightGray)
}