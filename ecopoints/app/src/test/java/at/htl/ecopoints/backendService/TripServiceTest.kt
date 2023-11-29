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
        val trip = createSampleTrip()

        // Act
        val response = tripService.createTrip(trip)

        // Assert
        assertEquals(201, response.code)
    }

    @Order(2)
    @Test
    fun updateTrip() {
        // Arrange
        val trip = createSampleTrip()


        // Act
        tripService.createTrip(trip)



        val response = tripService.updateTrip(trip, tripId)

        // Assert
        assertEquals(200, response.code)
    }

    @Order(5)
    @Test
    fun deleteTrip() {
        // Arrange
        val trip = createSampleTrip()


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

        // Act
        val retrievedTrip = tripService.getTripById(tripId)

        // Assert
        assertNotNull(retrievedTrip)
        assertEquals(1, retrievedTrip?.id)
    }

    @Order(4)
    @Test
    fun getAllTrips() {
        // Arrange

        // Act
        val retrievedTripList = tripService.getAllTrips()

        // Assert
        assertNotNull(retrievedTripList)
        assertEquals(2, retrievedTripList?.size)
    }

    private fun createSampleTrip(): Trip {
        return Trip(
            id = tripId,
            distance = 100.0,
            avgSpeed = 44.0,
            avgEngineRotation = 3000.0,
            rewardedEcoPoints = 10.0,
            date = Date()
        )
    }
}