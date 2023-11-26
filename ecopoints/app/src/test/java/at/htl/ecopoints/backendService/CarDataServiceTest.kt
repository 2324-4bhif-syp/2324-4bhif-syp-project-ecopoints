package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.Trip
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import org.junit.jupiter.api.Assertions.*
import org.junit.Before
import java.sql.Timestamp
import java.util.Date
import org.junit.Test


class CarDataServiceTest {
    private lateinit var carDataService: CarDataService

    @Before
    fun setup() {
        carDataService = CarDataService()
    }

    @Test
    fun createCarData() {
        // Arrange
        val carData = createSampleCarData()

        // Act
        val response = carDataService.createCarData(carData)

        // Assert
        assertEquals(201, response.code)
    }

    @Test
    fun updateCarData() {
        // Arrange
        val carData = createSampleCarData()

        // Act
        val response = carDataService.updateCarData(carData, 1)

        // Assert
        assertEquals(200, response.code)
    }

    @Test
    fun deleteCarData() {
        // Arrange

        // Act
        val response = carDataService.deleteCarData(1)

        // Assert
        assertEquals(204, response.code)
    }

    @Test
    fun getCarDataById() {
        // Arrange
        val carData = createSampleCarData()

        // Act
        val retrievedCarData = carDataService.getCarDataById(1)

        // Assert
        assertNotNull(retrievedCarData)
        assertEquals(carData.id, retrievedCarData?.id)
    }

    @Test
    fun getAllCarData() {
        // Arrange

        // Act
        val retrievedCarDataList = carDataService.getAllCarData()

        // Assert
        assertNotNull(retrievedCarDataList)
        assertEquals(2, retrievedCarDataList?.size)
    }

    private fun createSampleCarData(): CarData {
        return CarData(
            id = null,
            tripId = UUID.randomUUID(),
            longitude = 10.0,
            latitude = 20.0,
            currentEngineRPM = 3000.0,
            currentVelocity = 60.0,
            throttlePosition = 50.0,
            engineRunTime = "10:00",
            timeStamp = Timestamp(System.currentTimeMillis()),
            trip = Trip(
                id = null,
                distance = 100.0,
                avgSpeed = 44.0,
                avgEngineRotation = 3000.0,
                rewardedEcoPoints = 10.0,
                date = Date()
            )
        )
    }
}