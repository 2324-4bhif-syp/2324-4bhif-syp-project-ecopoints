package at.htl.ecopoints.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.UUID

data class CarData(
    val id: Long? = 0,

    @SerializedName("trip_id")
    val tripId: UUID = UUID.randomUUID(),

    val longitude: Double = 0.0,
    val latitude: Double = 0.0,

    @SerializedName("current_engine_rpm")
    val currentEngineRPM: Double = 0.0,

    @SerializedName("current_velocity")
    val currentVelocity: Double =0.0,

    @SerializedName("throttle_position")
    val throttlePosition: Double =0.0,

    @SerializedName("engine_run_time")
    val engineRunTime: String = "",

    @SerializedName("time_stamp")
    val timeStamp: Timestamp = Timestamp.valueOf("2021-01-01 00:00:00"),

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
