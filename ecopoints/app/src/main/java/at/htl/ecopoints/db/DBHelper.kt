package at.htl.ecopoints.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import at.htl.ecopoints.backendService.CarDataService
import at.htl.ecopoints.backendService.TripService
import java.sql.Timestamp
import java.util.Date
import java.util.UUID

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        createCarDataTable(db)
        createTripTable(db)
    }

    private fun createCarDataTable(db: SQLiteDatabase){
        val query = ("CREATE TABLE " + TABLE_CARDATA + " ("
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

    private fun createTripTable(db: SQLiteDatabase){
        val query = ("CREATE TABLE " + TABLE_TRIP + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                DISTANCE_COL + " REAL," +
                AVGSPEED_COL + " REAL," +
                AVGENGINE_ROTATION_COL + " REAL," +
                DATE_COL + " TEXT," +
                REWARDEDECOPOINTS_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        dropAndRecreateCarDataTable(db)
        dropAndRecreateTripTable(db)
    }

    private fun dropAndRecreateCarDataTable(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CARDATA")
        createCarDataTable(db)
    }

    private fun dropAndRecreateTripTable(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRIP")
        createTripTable(db)
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
        db.insert(TABLE_CARDATA, null, values)

        db.close()
    }

    fun addTrip(trip: Trip){
        val values = ContentValues()

        values.put(DISTANCE_COL, trip.distance)
        values.put(AVGSPEED_COL, trip.avgSpeed)
        values.put(AVGENGINE_ROTATION_COL, trip.avgEngineRotation)
        values.put(DATE_COL, trip.date.toString())
        values.put(REWARDEDECOPOINTS_COL, trip.rewardedEcoPoints)

        val db = this.writableDatabase
        db.insert(TABLE_TRIP, null, values)

        db.close()
    }

    fun getAllCarData(): ArrayList<CarData> {
        val db = this.readableDatabase
        val carDataList = ArrayList<CarData>()
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_CARDATA, null)

        if(cursor!!.moveToFirst()) {
            do {
                carDataList.add(getCarDataFromCursor(cursor!!))
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        return carDataList
    }

    fun getAllTrips(): ArrayList<Trip> {
        val db = this.readableDatabase
        val tripList = ArrayList<Trip>()
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_TRIP, null)

        if(cursor!!.moveToFirst()) {
            do {
                tripList.add(getTripFromCursor(cursor!!))
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        return tripList
    }

    private fun deleteAllCarData(){
        val db = this.writableDatabase
        db.delete(TABLE_CARDATA, null, null)
        db.close()
    }

    private fun deleteAllTrips(){
        val db = this.writableDatabase
        db.delete(TABLE_TRIP, null, null)
        db.close()
    }

    @SuppressLint("Range")
    fun syncCarDataWithBackend(){
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

    @SuppressLint("Range")
    fun syncTripsWithBackend(){
        val tripList = getAllTrips()

        if(tripList.size < 1)
            return

        val tripService = TripService()

        for(trip in tripList){
            tripService.createTrip(trip)
        }

        deleteAllTrips()
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

    @SuppressLint("Range")
    private fun getTripFromCursor(cursor: Cursor): Trip{
        return Trip(
            UUID.randomUUID(),
            cursor.getDouble(cursor.getColumnIndex(DISTANCE_COL)),
            cursor.getDouble(cursor.getColumnIndex(AVGSPEED_COL)),
            cursor.getDouble(cursor.getColumnIndex(AVGENGINE_ROTATION_COL)),
            Date(cursor.getString(cursor.getColumnIndex(DATE_COL))),
            cursor.getDouble(cursor.getColumnIndex(REWARDEDECOPOINTS_COL))
        )
    }

    companion object{
        private val DATABASE_NAME = "ECO_LOCAL_DB"
        private val DATABASE_VERSION = 1
        val TABLE_CARDATA = "ECO_CARDATA"
        val TABLE_TRIP = "ECO_TRIP"

        //CarData
        val ID_COL = "id"
        val LONGITUDE_COl = "longitude"
        val LATITUDE_COL = "latitude"
        val CURRENTENGINERPM_COL = "current_engine_rpm"
        val CURRENTVELOCITY_COL = "current_velocity"
        val THROTTLEPOSITION_COL = "throttle_position"
        val ENGINERUNTIME_COL = "engine_run_time"
        val TIMESTAMP_COL = "timestamp"

        //Trip
        val DISTANCE_COL = "distance"
        val AVGSPEED_COL = "avg_speed"
        val AVGENGINE_ROTATION_COL = "avg_engine_rotation"
        val DATE_COL = "date"
        val REWARDEDECOPOINTS_COL = "rewarded_eco_points"

    }
}