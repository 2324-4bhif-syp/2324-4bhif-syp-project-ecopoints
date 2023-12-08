package at.htl.ecopoints.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "ECO_CARDATA")
data class CarData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val longitude: Double,
    val latitude: Double,
    val currentEngineRPM: Double,
    val currentVelocity: Double,
    val throttlePosition: Double,
    val engineRunTime: String,
    val timestamp: Timestamp?
    )