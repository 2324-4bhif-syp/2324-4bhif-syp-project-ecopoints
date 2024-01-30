package at.htl.ecopoints.service
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import at.htl.ecopoints.model.User
import at.htl.ecopoints.R
import at.htl.ecopoints.model.Trip

class TripAdapter (private val context: Activity,
                   private val trips: Array<Trip>)
    : ArrayAdapter<Trip>(context, R.layout.trip_list_item, trips) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.trip_list_item, null)

        val date: TextView = view.findViewById(R.id.tripDate)
        val distance: TextView = view.findViewById(R.id.distance)

        date.text = trips[position].date.toGMTString();
        distance.text = trips[position].distance.toString()

        return view
    }
}