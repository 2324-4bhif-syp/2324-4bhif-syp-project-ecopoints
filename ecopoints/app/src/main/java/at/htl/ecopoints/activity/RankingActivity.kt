package at.htl.ecopoints.activity

import android.app.ActionBar.LayoutParams
import androidx.activity.ComponentActivity
import android.os.Bundle
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
import androidx.core.graphics.toColorInt
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme

class RankingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Ranking") }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayHeader()
                    DisplayRankingTable()

                    Box {
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
    fun DisplayRankingTable() {
        val layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        val tableLayout = createRankingTableLayout(layoutParams)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .height(400.dp)
                    .width(300.dp),
            ) {
                addContentView(tableLayout, layoutParams)
            }
        }
    }

    @Composable
    fun DisplayHeader() {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Ranking",
                color = Color.Green,
                fontSize = TextUnit(30f, TextUnitType.Sp)
            )
        }
    }

    private fun createRankingTableLayout(layoutParams: TableLayout.LayoutParams): TableLayout{
        val tableLayout = TableLayout(this)

        tableLayout.layoutParams = layoutParams

        val headerRow = TableRow(this)
        val headerParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headerRow.layoutParams = headerParams

        val rankHeader = createTextView("Rank", true)
        val nameHeader = createTextView("Name", true)
        val ecoPointsHeader = createTextView("Eco-Points", true)

        headerRow.addView(rankHeader)
        headerRow.addView(nameHeader)
        headerRow.addView(ecoPointsHeader)

        tableLayout.addView(headerRow)

        val dataRow1 = TableRow(this)
        val dataParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        dataRow1.layoutParams = dataParams

        // currently these are just test data-sets

        val dataTextView1 = createTextView("1", false)
        dataRow1.addView(dataTextView1)
        val dataTextView2 = createTextView("Armin", false)
        dataRow1.addView(dataTextView2)
        val dataTextView3 = createTextView("567", false)
        dataRow1.addView(dataTextView3)

        tableLayout.addView(dataRow1)

        val dataRow2 = TableRow(this)
        dataRow2.layoutParams = dataParams
        val dataTextView4 = createTextView("2", false)
        dataRow2.addView(dataTextView4)
        val dataTextView5 = createTextView("Linus", false)
        dataRow2.addView(dataTextView5)
        val dataTextView6 = createTextView("533", false)
        dataRow2.addView(dataTextView6)

        tableLayout.addView(dataRow2)

        return tableLayout
    }

    private fun createTextView(text: String, isHeader: Boolean): TextView {
        val textView = TextView(this)
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        textView.layoutParams = params
        textView.text = text
        textView.setPadding(30, 15, 30, 15)

        if (isHeader) {
            textView.setBackgroundColor(("#51B435").toColorInt())
            textView.setTextColor(resources.getColor(android.R.color.black))

        } else {
            textView.setBackgroundColor(("#F0F7F7").toColorInt())
        }

        return textView
    }
}
