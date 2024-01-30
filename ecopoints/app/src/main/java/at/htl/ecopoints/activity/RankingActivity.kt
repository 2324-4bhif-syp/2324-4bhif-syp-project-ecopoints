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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColorInt
import androidx.core.graphics.translationMatrix
import androidx.core.text.scale
import at.htl.ecopoints.model.User
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.service.RankingAdapter
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.R

class RankingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Ranking") }
            val spinnerItems = listOf("Option 1", "Option 2", "Option 3") // Add your spinner items here
            val selectedOption = remember { mutableStateOf(spinnerItems.first()) }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box{
                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@RankingActivity
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(106.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = {  },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            spinnerItems.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption.value = item
                                    }
                                ) {
                                    Text(text = item)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ShowRanking(context = this, activity = this@RankingActivity)
                }
            }
        }
    }

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
}
