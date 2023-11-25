package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.CarData

class CarDataService : Service() {
    private final val endPoint = "carData"

    fun createCarData(carData: CarData) {
        super.create(carData, endPoint)
    }

    fun updateCarData(carData: CarData, id: Long) {
        super.update(carData, endPoint, id)
    }

    fun deleteCarData(id: Long) {
        super.delete(endPoint, id)
    }

    fun getCarDataById(id: Long): CarData? {
        return super.getById<CarData>(endPoint, id)
    }

    fun getAllCarData(): List<CarData>? {
        return super.getAll<CarData>(endPoint)
    }
}