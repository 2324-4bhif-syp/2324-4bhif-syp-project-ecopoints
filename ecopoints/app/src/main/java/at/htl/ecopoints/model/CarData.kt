package at.htl.ecopoints.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.UUID

data class CarData(
    val id: Long?,

    @SerializedName("trip_id")
    val tripId: UUID,

    val longitude: Double,
    val latitude: Double,

    @SerializedName("current_engine_rpm")
    val currentEngineRPM: Double,

    @SerializedName("current_velocity")
    val currentVelocity: Double,

    @SerializedName("throttle_position")
    val throttlePosition: Double,

    @SerializedName("engine_run_time")
    val engineRunTime: String,

    @SerializedName("time_stamp")
    val timeStamp: Timestamp,

    //val trip: Trip
) {
    override fun toString(): String {
        return String.format(
            "CarData[id=%d, tripId=%s, longitude=%f, latitude=%f, currentEngineRPM=%f, currentVelocity=%f, " +
                    "throttlePosition=%f, engineRunTime=%s, timeStamp=%s",
            id,
            tripId,
            longitude,
            latitude,
            currentEngineRPM,
            currentVelocity,
            throttlePosition,
            engineRunTime,
            timeStamp,
            )
    }
}
