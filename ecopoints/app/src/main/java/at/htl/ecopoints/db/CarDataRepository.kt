package at.htl.ecopoints.db

class CarDataRepository(private val carDataDao: CarDataDao){
    suspend fun insertCarData(carData: CarData){
        carDataDao.insertCarData(carData)
    }

    suspend fun insertMultipleCarData(carDataList: List<CarData>){
        carDataDao.insertMultipleCarData(carDataList)
    }
    suspend fun getAllCarData(): List<CarData>{
        return carDataDao.getAllCarData()
    }
    suspend fun deleteAllCarData(){
        carDataDao.deleteAllCarData()
    }
}