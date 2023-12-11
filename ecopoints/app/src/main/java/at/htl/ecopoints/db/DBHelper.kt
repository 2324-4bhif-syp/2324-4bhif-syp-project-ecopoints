package at.htl.ecopoints.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import at.htl.ecopoints.backendService.CarDataService
import at.htl.ecopoints.backendService.TripService
import at.htl.ecopoints.model.Trip
import java.sql.Timestamp
import java.time.LocalDate
import java.util.Date
import java.util.UUID

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                LONGITUDE_COl + " REAL," +
                LATITUDE_COL + " REAL," +
                CURRENTENGINERPM_COL + " REAL," +
                CURRENTVELOCITY_COL + " REAL," +
                THROTTLEPOSITION_COL + " REAL," +
                ENGINERUNTIME_COL + " TEXT," +
                TIMESTAMP_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addCarData(carData: CarData){
        val values = ContentValues()

        values.put(LATITUDE_COL, carData.latitude)
        values.put(LONGITUDE_COl, carData.longitude)
        values.put(CURRENTENGINERPM_COL, carData.currentEngineRPM)
        values.put(CURRENTVELOCITY_COL, carData.currentVelocity)
        values.put(THROTTLEPOSITION_COL, carData.throttlePosition)
        values.put(ENGINERUNTIME_COL, carData.engineRunTime)
        values.put(TIMESTAMP_COL, carData.timestamp.toString())

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun getAllCarData(): ArrayList<CarData> {
        val db = this.readableDatabase
        val carDataList = ArrayList<CarData>()
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

        if(cursor!!.moveToFirst()) {
            do {
                carDataList.add(getCarDataFromCursor(cursor!!))
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        return carDataList
    }

    private fun deleteAllCarData(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    @SuppressLint("Range")
    fun syncWithBackend(){
        val carDataList = getAllCarData()

        if(carDataList.size < 1)
            return

        val trip = createTrip(carDataList)
        val id: UUID = trip.id

        val tripService = TripService()
        tripService.createTrip(trip)

        for(carData in carDataList){
            val carDataToSave = createCarData(carData, id)
            val carDataService = CarDataService()
            carDataService.createCarData(carDataToSave)
        }

        deleteAllCarData()
    }

    private fun createCarData(carData: CarData, tripId: UUID): at.htl.ecopoints.model.CarData{
        val id: Long = 0
        val longitude = carData.longitude
        val latitude = carData.latitude
        val currentEngineRPM = carData.currentEngineRPM
        val currentVelocity = carData.currentVelocity
        val throttlePosition = carData.throttlePosition
        val engineRunTime = carData.engineRunTime
        val timeStamp = Timestamp.valueOf(carData.timestamp.toString())

        return at.htl.ecopoints.model.CarData(id, tripId, longitude, latitude, currentEngineRPM, currentVelocity, throttlePosition, engineRunTime, timeStamp)
    }

    private fun createTrip(carDataList: ArrayList<CarData>): Trip{
        val id: UUID = UUID.randomUUID()
        val distance = 0.0
        val avgSpeed = 0.0
        var avgEngineRotation = 0.0
        val date: Date = Date()
        val rewardedEcoPoints = 0.0

        for(carData in carDataList) {
            avgEngineRotation += carData.currentEngineRPM
        }

        avgEngineRotation /= carDataList.size

        return Trip(id, distance, avgSpeed, avgEngineRotation, date, rewardedEcoPoints)
    }

    @SuppressLint("Range")
    private fun getCarDataFromCursor(cursor: Cursor): CarData{
        return CarData(
            0,
            cursor.getDouble(cursor.getColumnIndex(LATITUDE_COL)),
            cursor.getDouble(cursor.getColumnIndex(LONGITUDE_COl)),
            cursor.getDouble(cursor.getColumnIndex(CURRENTENGINERPM_COL)),
            cursor.getDouble(cursor.getColumnIndex(CURRENTVELOCITY_COL)),
            cursor.getDouble(cursor.getColumnIndex(THROTTLEPOSITION_COL)),
            cursor.getString(cursor.getColumnIndex(ENGINERUNTIME_COL)),
            Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(TIMESTAMP_COL)))
        )
    }

    companion object{
        private val DATABASE_NAME = "ECO_LOCAL_DB"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "ECO_CARDATA"

        val ID_COL = "id"
        val LONGITUDE_COl = "longitude"
        val LATITUDE_COL = "latitude"
        val CURRENTENGINERPM_COL = "current_engine_rpm"
        val CURRENTVELOCITY_COL = "current_velocity"
        val THROTTLEPOSITION_COL = "throttle_position"
        val ENGINERUNTIME_COL = "engine_run_time"
        val TIMESTAMP_COL = "timestamp"
    }
}