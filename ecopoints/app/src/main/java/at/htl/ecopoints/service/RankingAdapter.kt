package at.htl.ecopoints.service

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import at.htl.ecopoints.model.User
import at.htl.ecopoints.R

class RankingAdapter(private val context: Activity,
                     private val users: Array<User>)
    : ArrayAdapter<User>(context, R.layout.ranking_list_item, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.ranking_list_item, null)

        val userName: TextView = view.findViewById(R.id.user_name)
        val ecoPoints: TextView = view.findViewById(R.id.eco_points)
        val rank: TextView = view.findViewById(R.id.rank)

        userName.text = users[position].userName
        ecoPoints.text = users[position].ecoPoints.toString()
        rank.text = (position + 1).toString()

        return view
    }

}