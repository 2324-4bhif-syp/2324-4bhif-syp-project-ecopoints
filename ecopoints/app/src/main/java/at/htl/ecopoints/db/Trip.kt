package at.htl.ecopoints.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "ECO_TRIP")
class Trip (
    @PrimaryKey(autoGenerate = true)
    val id: UUID = UUID.randomUUID(),
    val distance: Double,
    val avgSpeed: Double,
    val avgEngineRotation: Double,
    val date: Date,
    val rewardedEcoPoints: Double
    )