package at.htl.ecopoints.activity

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Timelapse
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
import androidx.compose.ui.Alignment

@Composable
fun ProfileScreen(user: User, context: Context) {
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
                    ShowReturnBtn(context)

                    ShowProfileHeader(user)

                    ShowStatistics(user)
                }
            }
        }
    }
}

@Composable
private fun ShowReturnBtn(context: Context){
    IconButton(onClick = {
        val intent = Intent(context, RankingActivity::class.java)
        context.startActivity(intent)
    }) {
        androidx.compose.material3.Icon(
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


    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Column {
            Text(
                text = "Statistics",
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(28f, TextUnitType.Sp),
            )

            Row(
                modifier = Modifier.padding(top = 15.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Card(
                        border = BorderStroke(cardBorderWidth, cardBorderColor),
                        shape = RoundedCornerShape(cardCornerShapeSize),
                        modifier = Modifier
                            .size(width = cardWidth, height = cardHeight)
                    ) {
                        Text(text = "Trips")
                    }
                }

                Column {
                    Card(
                        border = BorderStroke(cardBorderWidth, cardBorderColor),
                        shape = RoundedCornerShape(cardCornerShapeSize),
                        modifier = Modifier.size(width = cardWidth, height = cardHeight)
                    ) {
                        Text(text = "Trips")
                    }
                }
            }

            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Card(
                        border = BorderStroke(cardBorderWidth, cardBorderColor),
                        shape = RoundedCornerShape(cardCornerShapeSize),
                        modifier = Modifier
                            .size(width = cardWidth, height = cardHeight)
                    ) {
                        Text(text = "Trips")
                    }
                }

                Column {
                    Card(
                        border = BorderStroke(cardBorderWidth, cardBorderColor),
                        shape = RoundedCornerShape(cardCornerShapeSize),
                        modifier = Modifier.size(width = cardWidth, height = cardHeight)
                    ) {
                        Text(text = "Trips")
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowProfileHeader(user: User) {
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
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.CenterVertically)
                )

                Text(
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
