package at.htl.ecopoints.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import at.htl.ecopoints.db.services.CarDataService
import at.htl.ecopoints.db.services.TripService
import at.htl.ecopoints.model.CarData
import at.htl.ecopoints.model.Trip
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.util.Date
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        createCarDataTable(db)
        createTripTable(db)
    }

    private fun createCarDataTable(db: SQLiteDatabase){
        val query = ("CREATE TABLE IF NOT EXISTS " + TABLE_CARDATA + " (" +
                CARDATA_ID_COL_PK + " INTEGER PRIMARY KEY, " +
                TRIP_ID_COL_FK + " TEXT, " +
                LONGITUDE_COl + " REAL," +
                LATITUDE_COL + " REAL," +
                CURRENTENGINERPM_COL + " REAL," +
                speed_COL + " REAL," +
                THROTTLEPOSITION_COL + " REAL," +
                ENGINERUNTIME_COL + " TEXT," +
                TIMESTAMP_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    private fun createTripTable(db: SQLiteDatabase) {
        val query = ("CREATE TABLE IF NOT EXISTS " + TABLE_TRIP + " ("
                + TRIP_ID_COL_PK + " TEXT PRIMARY KEY, " +
                CAR_ID_COL_FK + " INTEGER," +
                USER_ID_COL_FK + " INTEGER," +
                DISTANCE_COL + " REAL," +
                AVGSPEED_COL + " REAL," +
                AVGENGINE_ROTATION_COL + " REAL," +
                START_DATE_COL + " TEXT," +
                END_DATE_COL + " TEXT," +
                REWARDEDECOPOINTS_COL + " REAL" + ")")

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

        values.put(CARDATA_ID_COL_PK, carData.id.toString())
        values.put(TRIP_ID_COL_FK, carData.tripId.toString())
        values.put(LONGITUDE_COl, carData.longitude)
        values.put(LATITUDE_COL, carData.latitude)
        values.put(CURRENTENGINERPM_COL, carData.currentEngineRPM)
        values.put(speed_COL, carData.speed)
        values.put(THROTTLEPOSITION_COL, carData.throttlePosition)
        values.put(ENGINERUNTIME_COL, carData.engineRunTime)
        values.put(TIMESTAMP_COL, carData.timeStamp.toString())

        val db = this.writableDatabase
        db.insert(TABLE_CARDATA, null, values)

        db.close()
    }

    fun addTrip(trip: Trip){
        val values = ContentValues()

        values.put(TRIP_ID_COL_PK, trip.id.toString())
        values.put(CAR_ID_COL_FK, trip.carId)
        values.put(USER_ID_COL_FK, trip.userId)
        values.put(DISTANCE_COL, trip.distance)
        values.put(AVGSPEED_COL, trip.avgSpeed)
        values.put(AVGENGINE_ROTATION_COL, trip.avgEngineRotation)
        values.put(START_DATE_COL, trip.start.toString())
        values.put(END_DATE_COL, trip.end.toString())
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

    fun getTopThreeTrips(): List<Trip> {
        val db = this.readableDatabase
        val tripList = mutableListOf<Trip>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TRIP ORDER BY $REWARDEDECOPOINTS_COL DESC LIMIT 3", null)

        if(cursor.moveToFirst()) {
            do {
                tripList.add(getTripFromCursor(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tripList
    }

    fun getAllCarDataForTrip(tripId: UUID): List<CarData> {

        var carDatas = getAllCarData();
        val carDataList = mutableListOf<CarData>()

        for (carData in carDatas) {
            if (carData.tripId == tripId) {
                carDataList.add(carData)
            }
        }

        return carDataList
    }


    fun updateTripValues(tripId: UUID) {
        val db = this.readableDatabase

        val carDataList = getAllCarDataForTrip(tripId)

        if (carDataList.isNotEmpty()) {
            val tripValues = ContentValues()

            val avgSpeed = carDataList.map { it.speed }.average()
            val avgEngineRotation = carDataList.map { it.currentEngineRPM }.average()
            val totalDistance = calculateTotalDistance(carDataList)

            val roundedAvgSpeed = BigDecimal(avgSpeed).setScale(2, RoundingMode.HALF_UP).toDouble()
            val roundedAvgEngineRotation = BigDecimal(avgEngineRotation).setScale(2, RoundingMode.HALF_UP).toDouble()
            val roundedTotalDistance = BigDecimal(totalDistance).setScale(2, RoundingMode.HALF_UP).toDouble()


            tripValues.put(AVGSPEED_COL, roundedAvgSpeed)
            tripValues.put(AVGENGINE_ROTATION_COL, roundedAvgEngineRotation)
            tripValues.put(DISTANCE_COL, roundedTotalDistance)


            db.update(TABLE_TRIP, tripValues, "$TRIP_ID_COL_PK=?", arrayOf(tripId.toString()))
        }

        db.close()
    }

    private fun calculateTotalDistance(carDataList: List<CarData>): Double {
        var totalDistance = 0.0

        for (i in 0 until carDataList.size - 1) {
            val carData1 = carDataList[i]
            val carData2 = carDataList[i + 1]

            val distanceBetweenPoints = calculateDistance(
                carData1.latitude, carData1.longitude,
                carData2.latitude, carData2.longitude
            )

            totalDistance += distanceBetweenPoints
        }

        return totalDistance
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
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

    private fun createCarData(carData: CarData, tripId: UUID): CarData{
        val id: Long = 0
        val longitude = carData.longitude
        val latitude = carData.latitude
        val currentEngineRPM = carData.currentEngineRPM
        val speed = carData.speed
        val throttlePosition = carData.throttlePosition
        val engineRunTime = carData.engineRunTime
        val timeStamp = Timestamp.valueOf(carData.timeStamp.toString())

        return CarData(id, tripId, longitude, latitude, currentEngineRPM, speed, throttlePosition, engineRunTime, timeStamp)
    }

    private fun createTrip(carDataList: ArrayList<CarData>): Trip {
        val id: UUID = UUID.randomUUID()
        val carId = 0L
        val userId = 0L
        val distance = 0.0
        val avgSpeed = 0.0
        var avgEngineRotation = 0.0
        val start: Date = Date()
        val end: Date = Date()
        val rewardedEcoPoints = 0.0

        for(carData in carDataList) {
            avgEngineRotation += carData.currentEngineRPM
        }

        avgEngineRotation /= carDataList.size

        return Trip(id, carId, userId, distance, avgSpeed, avgEngineRotation, start, end, rewardedEcoPoints)
    }

    @SuppressLint("Range")
    private fun getCarDataFromCursor(cursor: Cursor): CarData {
        val tripIdString = cursor.getString(cursor.getColumnIndex(TRIP_ID_COL_FK))
        val tripId = if (tripIdString != null) UUID.fromString(tripIdString) else UUID.randomUUID()

        return CarData(
            0,
            tripId,
            cursor.getDouble(cursor.getColumnIndex(LONGITUDE_COl)),
            cursor.getDouble(cursor.getColumnIndex(LATITUDE_COL)),
            cursor.getDouble(cursor.getColumnIndex(CURRENTENGINERPM_COL)),
            cursor.getDouble(cursor.getColumnIndex(speed_COL)),
            cursor.getDouble(cursor.getColumnIndex(THROTTLEPOSITION_COL)),
            cursor.getString(cursor.getColumnIndex(ENGINERUNTIME_COL)),
            Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(TIMESTAMP_COL)))
        )
    }


    @SuppressLint("Range")
    private fun getTripFromCursor(cursor: Cursor): Trip{
        val tripIdString = cursor.getString(cursor.getColumnIndex(TRIP_ID_COL_PK))
        val tripId = if (tripIdString != null) UUID.fromString(tripIdString) else UUID.randomUUID()

        return Trip(
            tripId,
            cursor.getLong(cursor.getColumnIndex(CAR_ID_COL_FK)),
            cursor.getLong(cursor.getColumnIndex(USER_ID_COL_FK)),
            cursor.getDouble(cursor.getColumnIndex(DISTANCE_COL)),
            cursor.getDouble(cursor.getColumnIndex(AVGSPEED_COL)),
            cursor.getDouble(cursor.getColumnIndex(AVGENGINE_ROTATION_COL)),
            Date(cursor.getString(cursor.getColumnIndex(START_DATE_COL))),
            Date(cursor.getString(cursor.getColumnIndex(END_DATE_COL))),
            cursor.getDouble(cursor.getColumnIndex(REWARDEDECOPOINTS_COL))
        )
    }

    companion object{
        private val DATABASE_NAME = "ECO_LOCAL_DB"
        private val DATABASE_VERSION = 1
        val TABLE_CARDATA = "ECO_CARDATA"
        val TABLE_TRIP = "ECO_TRIP"

        //CarData
        val CARDATA_ID_COL_PK = "id"
        val TRIP_ID_COL_FK = "trip_id"
        val LONGITUDE_COl = "longitude"
        val LATITUDE_COL = "latitude"
        val CURRENTENGINERPM_COL = "current_engine_rpm"
        val speed_COL = "current_velocity"
        val THROTTLEPOSITION_COL = "throttle_position"
        val ENGINERUNTIME_COL = "engine_run_time"
        val TIMESTAMP_COL = "timestamp"

        //Trip
        val TRIP_ID_COL_PK = "id"
        val CAR_ID_COL_FK = "car_id"
        val USER_ID_COL_FK = "user_id"
        val DISTANCE_COL = "distance"
        val AVGSPEED_COL = "avg_speed"
        val AVGENGINE_ROTATION_COL = "avg_engine_rotation"
        val START_DATE_COL = "start_date"
        val END_DATE_COL = "end_date"
        val REWARDEDECOPOINTS_COL = "rewarded_eco_points"

    }
}