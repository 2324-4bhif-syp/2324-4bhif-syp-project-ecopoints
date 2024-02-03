package at.htl.ecopoints.activity

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColorInt
import androidx.core.graphics.translationMatrix
import androidx.core.text.scale
import at.htl.ecopoints.model.User
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.R

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
        // JUST TESTING-DATA
        val users: Array<User> = arrayOf(
            User(null, "Joe", "123", 547.1),
            User(null, "Mary", "123", 533.9),
            User(null, "Chris", "123", 513.4),
            User(null, "John", "123", 431.3),
            User(null, "Hary", "123", 347.1),
            User(null, "Jane", "123", 333.9),
            User(null, "Max", "123", 313.4),
            User(null, "Mike", "123", 231.3))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 150.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            users.forEach { user ->
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ){
                    Text(
                        text = user.userName,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Text(
                        text = user.ecoPoints.toString(),
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
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
                        var size:Dp = 60.dp;
                        if(selectedOption.value == option.key) {
                            size = 80.dp;
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

    /*
    @Composable
    fun ShowRanking(context: Context, activity: Activity) {
        val listView = ListView(context)

        // JUST TESTING-DATA
        val users: Array<User> = arrayOf(
            User(null, "Joe", "123", 547.1),
            User(null, "Mary", "123", 533.9),
            User(null, "Chris", "123", 513.4),
            User(null, "John", "123", 431.3),
        )/*
            User(null, "Hary", "123", 347.1),
            User(null, "Jane", "123", 333.9),
            User(null, "Max", "123", 313.4),
            User(null, "Mike", "123", 231.3))
*/
        listView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        listView.adapter = RankingAdapter(activity, users)
        listView.divider = null
        listView.isVerticalScrollBarEnabled = true
        val dialog: Dialog = Dialog(context)

        listView.isClickable = true
        listView.setOnItemClickListener { parent, view, position, id ->
            dialog.setContentView(R.layout.user_ranking_popup)
            dialog.findViewById<TextView>(R.id.user_name).text = users[position].userName
            dialog.findViewById<TextView>(R.id.rank).text = (position + 1).toString()
            dialog.findViewById<TextView>(R.id.eco_points).text = users[position].ecoPoints.toString()
            dialog.findViewById<TextView>(R.id.driven_distance).text = "Driven Distance: " + "345" + " km"

            val rank = dialog.findViewById<TextView>(R.id.rank)

            val items = arrayOf("BMW (316d)", "Opel (Kadett)")
            val builder =  SpannableStringBuilder()

            builder.append("Driven Cars\n")

            items.forEach { item ->
                builder.scale(0.85f, { append(
                    item + "\n",
                    BulletSpan(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                ) })
            }

            dialog.findViewById<TextView>(R.id.driven_cars).setText(builder,  TextView.BufferType.SPANNABLE)

            if(position == 0) {
                rank.setTextColor(android.graphics.Color.parseColor("#FFD700"))
            } else if(position == 1) {
                rank.setTextColor(android.graphics.Color.parseColor("#C0C0C0"))
            } else if(position == 2) {
                rank.setTextColor(android.graphics.Color.parseColor("#CD7F32"))
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.Transparent.hashCode()))
            dialog.show()
        }

        this.addContentView(listView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    */
}
