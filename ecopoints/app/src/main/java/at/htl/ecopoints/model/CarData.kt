package at.htl.ecopoints.model

import java.sql.Timestamp
import java.util.UUID

data class CarData(
    val id: Long,
    val tripId: UUID,
    val longitude: Double,
    val latitude: Double,
    val currentEngineRPM: Double,
    val currentVelocity: Double,
    val throttlePosition: Double,
    val engineRunTime: String,
    val timeStamp: Timestamp,
    val trip: Trip
)
