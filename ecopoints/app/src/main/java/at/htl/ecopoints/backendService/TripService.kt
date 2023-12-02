package at.htl.ecopoints.backendService

import android.util.Log
import at.htl.ecopoints.model.Trip
import okhttp3.Response
import java.util.UUID


class TripService: Service() {
    private final val endPoint: String = "trip"

    fun createTrip(trip: Trip): Response{
        return super.create(trip, endPoint)
    }

    fun updateTrip(trip: Trip, id: UUID): Response{
        return super.update(trip, endPoint, id.toString())
    }

    fun deleteTrip(id: UUID): Response{
        return super.delete(endPoint, id.toString())
    }

    fun getTripById(id: UUID): Trip? {
        val trip = super.getById<Trip>(endPoint, id.toString())
        return trip
    }

    fun getAllTrips(): List<Trip>? {
        val trips = super.getAll<Trip>(endPoint)
        return trips
    }
}