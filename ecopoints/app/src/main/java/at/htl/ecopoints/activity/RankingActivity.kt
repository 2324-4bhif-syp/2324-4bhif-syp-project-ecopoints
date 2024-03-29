package at.htl.ecopoints.activity

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import at.htl.ecopoints.model.User
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.R
import at.htl.ecopoints.model.FuelType

class RankingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Ranking") }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ShowOptionsForRankType()

                    DisplayRanking()

                    Box{
                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@RankingActivity
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayRanking(){
        var showDetailedRankPopup = remember { mutableStateOf(false) }
        var selectedUser = remember { mutableStateOf<User?>(null) }

        // JUST TESTING-DATA
        val users: Array<User> = arrayOf(
            User(1, "Joe", "123", 547.1),
            User(2, "Mary", "123", 533.9),
            User(3, "Chris", "123", 513.4),
            User(4, "John", "123", 431.3),
            User(5, "Hary", "123", 347.1),
            User(6, "Jane", "123", 333.9),
            User(7, "Max", "123", 313.4),
            User(8, "Mike", "123", 231.3),
            User(9, "Chloe", "123", 133.9),
            User(10, "Courtney", "123", 113.4),
            User(11, "Lisa", "123", 91.3))

        val ranks = HashMap<User, Painter>();
        ranks.put(users[0], painterResource(id = R.drawable.ranking_place_1))
        ranks.put(users[1], painterResource(id = R.drawable.ranking_place_2))
        ranks.put(users[2], painterResource(id = R.drawable.ranking_place_3))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 200.dp, 0.dp, 0.dp)
                .verticalScroll(rememberScrollState()),

        ){
            users.forEach { user ->
                Button(
                    onClick = {
                        showDetailedRankPopup.value = true
                        selectedUser.value = user
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ){

                    Box(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .width(40.dp),
                    ) {
                        ranks.get(user)?.let {
                            Image(
                                painter = it,
                                contentDescription = "Rank",
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp)
                            )
                        }
                        if (ranks.get(user) == null) {
                            Text(
                                text = (users.indexOf(user) + 1).toString(),
                                fontSize = TextUnit(20f, TextUnitType.Sp),
                                modifier = Modifier
                                    .padding(8.dp),
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.no_profile_pic),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .width(35.dp)
                            .height(35.dp)
                            .clip(RoundedCornerShape(30.dp))
                    )

                    Text(
                        text = user.userName,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = Modifier
                            .padding(8.dp)
                            .padding(start = 15.dp)
                            .weight(1f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = user.ecoPoints.toString(),
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                    )
                }
            }
        }

        if (showDetailedRankPopup.value) {
            ProfileScreen(user = selectedUser.value!!, context = this@RankingActivity)
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ShowFuelTypeDropdown() {
        var expanded = remember { mutableStateOf(false) }

        var diesel: FuelType = FuelType("Diesel")
        var petrol: FuelType = FuelType("Petrol")
        val fuelTypes = listOf<FuelType>(diesel, petrol)

        val selectedItems = remember {
            mutableStateListOf<FuelType>(diesel, petrol)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp)
        ) {
            IconButton(onClick = { expanded.value = true }) {
                Icon(
                    painterResource(id = R.drawable.ranking_category_filter),
                    contentDescription = "Localized description",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp))
            }

            if(expanded.value) {
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                ) {
                    fuelTypes.forEach { fuelType ->
                        val isSelected = selectedItems.contains(fuelType)

                        androidx.compose.material3.ListItem(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    if (isSelected) {
                                        selectedItems.remove(fuelType)
                                    } else {
                                        selectedItems.add(fuelType)
                                    }
                                }
                            ),
                            leadingContent = {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            headlineContent = {
                                Text(
                                    text = fuelType.name
                                )
                            },
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun ShowOptionsForRankType(){
        val options = HashMap<String, Painter>();
        options.put("CO₂-Consumption", painterResource(id = R.drawable.ranking_category_co2))
        options.put("Eco-Points", painterResource(id = R.drawable.ranking_category_ecopoints))

        val selectedOption = remember { mutableStateOf(options.keys.first()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ShowFuelTypeDropdown()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                options.forEach() { option ->
                    Button(
                        onClick = {
                            selectedOption.value = option.key
                        },
                        colors = ButtonDefaults.buttonColors(
                             Color.Transparent
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                    ){
                        var size:Dp = 50.dp;
                        if(selectedOption.value == option.key) {
                            size = 70.dp;
                        }

                        Image(
                            painter = option.value,
                            contentDescription = option.key,
                            modifier = Modifier
                                .width(size)
                                .height(size)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = selectedOption.value,
                    fontSize = TextUnit(25f, TextUnitType.Sp),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }

            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }

}
