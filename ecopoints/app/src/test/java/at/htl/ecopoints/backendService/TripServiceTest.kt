package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.Trip
import org.junit.Before
import org.junit.jupiter.api.Assertions.*

import org.junit.Test
import org.junit.jupiter.api.Order
import java.util.Date
import java.util.UUID

class TripServiceTest {
    private lateinit var tripService: TripService
    private final val tripId: UUID = UUID.fromString("f7a3d7a0-4f1a-4b1a-9e5a-5a5a5a5a5a5a")

    @Before
    fun setup() {
        tripService = TripService()
    }


    @Order(1)
    @Test
    fun createTrip() {
        // Arrange
        val trip = createSampleTrip(44.0)

        // Act
        val response = tripService.createTrip(trip)

        tripService.deleteTrip(tripId)
        // Assert
        assertEquals(201, response.code)
    }

    @Order(2)
    @Test
    fun updateTrip() {
        // Arrange
        val trip = createSampleTrip(44.0)
        val trip2 = createSampleTrip(55.0)

        // Act
        tripService.createTrip(trip)

        val response = tripService.updateTrip(trip2, tripId)

        tripService.deleteTrip(tripId)

        // Assert
        assertEquals(200, response.code)
    }

    @Order(5)
    @Test
    fun deleteTrip() {
        // Arrange
        val trip = createSampleTrip(44.0)

        // Act
        tripService.createTrip(trip)

        val response = tripService.deleteTrip(tripId)

        // Assert
        assertEquals(204, response.code)
    }

    @Order(3)
    @Test
    fun getTripById() {
        // Arrange
        val trip = createSampleTrip(44.0)

        // Act
        tripService.createTrip(trip)

        val retrievedTrip = tripService.getTripById(tripId)

        tripService.deleteTrip(tripId)

        // Assert
        assertNotNull(retrievedTrip)
        assertEquals(tripId, retrievedTrip?.id)
    }

    @Order(4)
    @Test
    fun getAllTrips() {
        // Arrange
        val trip = createSampleTrip(44.0)

        // Act
        tripService.createTrip(trip)
        val retrievedTripList = tripService.getAllTrips()

        tripService.deleteTrip(tripId)

        // Assert
        assertNotNull(retrievedTripList)
        assertEquals(1, retrievedTripList?.size)
    }

    private fun createSampleTrip(avgSpeed: Double): Trip {
        return Trip(
            id = tripId,
            distance = 100.0,
            avgSpeed = avgSpeed,
            avgEngineRotation = 3000.0,
            rewardedEcoPoints = 10.0,
            startDate = Date()
        )
    }
}