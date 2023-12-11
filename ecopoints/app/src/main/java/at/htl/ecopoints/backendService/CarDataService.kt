package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.CarData
import okhttp3.Response

class CarDataService : Service() {
    private final val endPoint = "carData"

    fun createCarData(carData: CarData): Response {
        return super.create(carData, endPoint)
    }

    fun updateCarData(carData: CarData, id: Long): Response {
        return super.update(carData, endPoint, id.toString())
    }

    fun deleteCarData(id: Long): Response {
        return super.delete(endPoint, id.toString())
    }

    fun getCarDataById(id: Long): CarData? {
        return super.getById<CarData>(endPoint, id.toString())
    }

    fun getAllCarData(): List<CarData>? {
        return super.getAll<CarData>(endPoint)
    }
}