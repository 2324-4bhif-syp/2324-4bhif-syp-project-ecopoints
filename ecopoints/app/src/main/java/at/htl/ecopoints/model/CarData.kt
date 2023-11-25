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
) {
    override fun toString(): String {
        return String.format(
            "CarData[id=%d, tripId=%s, longitude=%f, latitude=%f, currentEngineRPM=%f, currentVelocity=%f, " +
                    "throttlePosition=%f, engineRunTime=%s, timeStamp=%s, trip=%s]",
            id,
            tripId,
            longitude,
            latitude,
            currentEngineRPM,
            currentVelocity,
            throttlePosition,
            engineRunTime,
            timeStamp,
            trip)
    }
}
