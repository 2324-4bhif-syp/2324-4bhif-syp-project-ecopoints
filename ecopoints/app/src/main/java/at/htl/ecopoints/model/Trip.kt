package at.htl.ecopoints.model

import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class Trip(

    @SerializedName("id")
    val id: UUID,

    @SerializedName("car_id")
    val carId: Long,

    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("distance")
    val distance: Double,

    @SerializedName("avg_speed")
    val avgSpeed: Double,

    @SerializedName("avg_engine_rotation")
    val avgEngineRotation: Double,

    @SerializedName("start_date")
    val startDate: Date,

    @SerializedName("end_date")
    val endDate: Date,

    @SerializedName("rewarded_eco_points")
    val rewardedEcoPoints: Double,

    ) {
    override fun toString(): String {
        return String.format(
            "Trip[id=%s, carId=%d, userId=%d, distance=%f, avgSpeed=%f, avgEngineRotation=%f, startDate=%s, endDate=%s, rewardedEcoPoints=%f]",
            id,
            carId,
            userId,
            distance,
            avgSpeed,
            avgEngineRotation,
            startDate,
            endDate,
            rewardedEcoPoints,
        )
    }
}
