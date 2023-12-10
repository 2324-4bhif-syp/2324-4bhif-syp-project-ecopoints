package at.htl.ecopoints.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

    fun getAllCarData(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
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