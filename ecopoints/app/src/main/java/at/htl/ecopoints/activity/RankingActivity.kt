package at.htl.ecopoints.activity

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme

class RankingActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tableLayout = TableLayout(this)
        val layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        tableLayout.layoutParams = layoutParams

        val headerRow = TableRow(this)
        val headerParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headerRow.layoutParams = headerParams

        for (i in 1..3) {
            val headerTextView = createTextView("Header $i", true)
            headerRow.addView(headerTextView)
        }

        tableLayout.addView(headerRow)

        for (rowIndex in 1..5) {
            val dataRow = TableRow(this)
            val dataParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            dataRow.layoutParams = dataParams

            for (colIndex in 1..3) {
                val dataTextView = createTextView("Data $rowIndex-$colIndex", false)
                dataRow.addView(dataTextView)
            }

            tableLayout.addView(dataRow)
        }

        setContent{
            val (currentScreen, setCurrentScreen) = remember { mutableStateOf("Ranking") }

            val tableLayout = TableLayout(this)
            val layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableLayout.layoutParams = layoutParams

            val headerRow = TableRow(this)
            val headerParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            headerRow.layoutParams = headerParams

            for (i in 1..3) {
                val headerTextView = createTextView("Header $i", true)
                headerRow.addView(headerTextView)
            }

            tableLayout.addView(headerRow)

            for (rowIndex in 1..5) {
                val dataRow = TableRow(this)
                val dataParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                dataRow.layoutParams = dataParams

                for (colIndex in 1..3) {
                    val dataTextView = createTextView("Data $rowIndex-$colIndex", false)
                    dataRow.addView(dataTextView)
                }

                tableLayout.addView(dataRow)
            }

            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    this.addContentView(tableLayout, layoutParams)

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

    private fun createTextView(text: String, isHeader: Boolean): TextView {
        val textView = TextView(this)
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        textView.layoutParams = params
        textView.text = text
        textView.gravity = Gravity.CENTER

        if (isHeader) {
            textView.setBackgroundResource(android.R.color.white)
            textView.setTextColor(resources.getColor(android.R.color.black))
        } else {
            textView.setBackgroundResource(android.R.drawable.edit_text)
        }

        return textView
    }
}