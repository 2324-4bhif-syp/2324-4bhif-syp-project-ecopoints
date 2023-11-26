package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.Trip
import org.junit.Before
import org.junit.jupiter.api.Assertions.*

import org.junit.Test
import java.util.Date

class TripServiceTest {
    private lateinit var tripService: TripService

    @Before
    fun setup() {
        tripService = TripService()
    }

    @Test
    fun createTrip() {
        // Arrange
        val trip = createSampleTrip()

        // Act
        val response = tripService.createTrip(trip)

        // Assert
        assertEquals(201, response.code)
    }

    @Test
    fun updateTrip() {
        // Arrange
        val trip = createSampleTrip()

        // Act
        val response = tripService.updateTrip(trip, 1)

        // Assert
        assertEquals(200, response.code)
    }

    @Test
    fun deleteTrip() {
        // Arrange

        // Act
        val response = tripService.deleteTrip(1)

        // Assert
        assertEquals(204, response.code)
    }

    @Test
    fun getTripById() {
        // Arrange

        // Act
        val retrievedTrip = tripService.getTripById(1)

        // Assert
        assertNotNull(retrievedTrip)
        assertEquals(1, retrievedTrip?.id)
    }

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
            id = null,
            distance = 100.0,
            avgSpeed = 44.0,
            avgEngineRotation = 3000.0,
            rewardedEcoPoints = 10.0,
            date = Date()
        )
    }
}