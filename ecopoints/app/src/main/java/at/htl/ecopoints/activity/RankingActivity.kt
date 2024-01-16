package at.htl.ecopoints.activity

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
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
import at.htl.ecopoints.model.User
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.service.RankingAdapter
import at.htl.ecopoints.ui.theme.EcoPointsTheme

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
                    Box{
                        BottomNavBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { newScreen -> setCurrentScreen(newScreen) },
                            context = this@RankingActivity
                        )
                    }

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

        listView.isClickable = true
        listView.setOnItemClickListener { parent, view, position, id ->
            val userName = users[position].userName

            val i = Intent(context, RankingUserActivity::class.java)
            i.putExtra("userName", userName)
            context.startActivity(i)
        }

        this.addContentView(listView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }
}
