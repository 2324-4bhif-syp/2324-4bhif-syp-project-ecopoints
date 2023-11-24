package at.htl.ecopoints.backendService

import android.util.Log
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.Trip


class TripService: Service() {
    private final val endPoint: String = "trip"

    fun createTrip(trip: Trip){
        super.create(trip, endPoint)
    }

    fun updateTrip(trip: Trip, id: Long){
        super.update(trip, endPoint, id)
    }

    fun deleteTrip(id: Long){
        super.delete(endPoint, id)
    }
}