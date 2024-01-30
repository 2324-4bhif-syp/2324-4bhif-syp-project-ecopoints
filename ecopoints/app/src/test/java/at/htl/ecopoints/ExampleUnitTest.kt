package at.htl.ecopoints

import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.GasData
import at.htl.ecopoints.service.TankerkoenigApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testApi() = runBlocking {
        val apiClient = TankerkoenigApiClient()
        val gasStationData = apiClient.getApiData()

        println("Diesel Preis: ${gasStationData.diesel}")
        println("E5 Preis: ${gasStationData.e5}")
    }
}