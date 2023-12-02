package at.htl.ecopoints.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "SQLLiteCarDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "CAR_DATA";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LONGITUDE = "car_longitude";
    private static final String COLUMN_LATITUDE = "car_latitude";
    private static final String COLUMN_CURRENTENGINERPM = "car_currentrpm";
    private static final String COLUMN_CURRENTVELOCITY = "car_currentvelocity";
    private static final String COLUMN_THROTTLEPOSITION = "car_throttleposition";
    private static final String COLUMN_ENGINERUNTIME = "car_engineruntime";
    private static final String COLUMN_TIMESTAMP = "car_timestamp";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LONGITUDE + " TEXT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_CURRENTENGINERPM + " TEXT, " +
                COLUMN_CURRENTVELOCITY + " TEXT, " +
                COLUMN_THROTTLEPOSITION + " TEXT, " +
                COLUMN_ENGINERUNTIME + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_NAME));
        onCreate(db);
    }
}
