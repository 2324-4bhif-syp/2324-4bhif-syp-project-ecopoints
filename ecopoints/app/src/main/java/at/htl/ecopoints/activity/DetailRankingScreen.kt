package at.htl.ecopoints.activity

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.model.User
import at.htl.ecopoints.R
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.material3.MaterialTheme.colorScheme

@Composable
fun ProfileScreen(user: User, context: Context) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
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

                    ProfileHeader(user)
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
            tint = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ProfileHeader(user: User) {
    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Text(
            modifier = Modifier.weight(1f),
            text = user.userName,
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(30f, TextUnitType.Sp)
        )

        Image(
            painter = painterResource(id = R.drawable.no_profile_pic),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(80.dp))
        )
    }
    androidx.compose.material3.Divider(thickness = 1.dp, color = Color.LightGray)
}
