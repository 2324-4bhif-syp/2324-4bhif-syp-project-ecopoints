package at.htl.ecopoints.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.google.android.gms.common.util.CollectionUtils.listOf
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

@Dao
class CarDataDao(private val connection: Connection){
    @Insert
    fun insertCarData(carData: CarData){
        val query = "Insert into car_data (id, longitude, latitude, current_engine_rpm, current_velocity, throttle_position, engine_run_time, timestamp " +
                      "values (?,?,?,?,?,?,?,?);"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, carData.id)
        preparedStatement.setDouble(2, carData.longitude)
        preparedStatement.setDouble(3, carData.latitude)
        preparedStatement.setDouble(4, carData.currentEngineRPM)
        preparedStatement.setDouble(5, carData.currentVelocity)
        preparedStatement.setDouble(6, carData.throttlePosition)
        preparedStatement.setString(7, carData.engineRunTime)
        preparedStatement.setTimestamp(8, carData.timestamp)

        preparedStatement.executeUpdate()
    }
    @Insert
    fun insertMultipleCarData(carDataList: List<CarData>){
        for (carData in carDataList)
            insertCarData(carData)
    }
    @Query("Delete from car_data")
    fun deleteAllCarData(){
        val query = "Delete from car_data"
        val statement = connection.createStatement()
        statement.executeUpdate(query)
    }
    @Query("Select * from car_data")
    fun getAllCarData(): List<CarData>{
        val query = "Select * from car_data"
        val statement = connection.createStatement()
        val resultSet: ResultSet = statement.executeQuery(query)

        val carDataList = listOf<CarData>()
        while(resultSet.next()){
            val carData = CarData(
                resultSet.getLong("id"),
                resultSet.getDouble("longitude"),
                resultSet.getDouble("latitude"),
                resultSet.getDouble("current_engine_rpm"),
                resultSet.getDouble("current_velocity"),
                resultSet.getDouble("throttle_position"),
                resultSet.getString("engine_run_time"),
                resultSet.getTimestamp("timestamp")
            )
            carDataList.add(carData)
        }
        return carDataList
    }
}