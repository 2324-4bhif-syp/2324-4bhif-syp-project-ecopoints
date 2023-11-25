package at.htl.ecopoints.backendService

import android.util.Log
import at.htl.ecopoints.model.Trip
import okhttp3.Response


class TripService: Service() {
    private final val endPoint: String = "trip"

    fun createTrip(trip: Trip): Response{
        return super.create(trip, endPoint)
    }

    fun updateTrip(trip: Trip, id: Long): Response{
        return super.update(trip, endPoint, id)
    }

    fun deleteTrip(id: Long): Response{
        return super.delete(endPoint, id)
    }

    fun getTripById(id: Long): Trip? {
        val trip = super.getById<Trip>(endPoint, id)
        Log.i("TripService", trip.toString())
        return trip
    }

    fun getAllTrips(): List<Trip>? {
        val trips = super.getAll<Trip>(endPoint)
        Log.i("TripService", trips.toString())
        return trips
    }
}