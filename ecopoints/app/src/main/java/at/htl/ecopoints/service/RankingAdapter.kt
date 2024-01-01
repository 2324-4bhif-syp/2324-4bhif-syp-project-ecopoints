package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class RankingAdapter(private val context: Activity,
                     private val userNames: Array<String>,
                     private val ecoPoints: Array<Double>)
    : ArrayAdapter<String>(context, android.R.layout.simple_list_item_2, userNames) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(android.R.layout.simple_list_item_2, null, true)

        val userNameTextView = rowView.findViewById(android.R.id.text1) as TextView
        val ecoPointsTextView = rowView.findViewById(android.R.id.text2) as TextView

        userNameTextView.text = userNames[position]
        ecoPointsTextView.text = ecoPoints[position].toString()

        return rowView
    }

}