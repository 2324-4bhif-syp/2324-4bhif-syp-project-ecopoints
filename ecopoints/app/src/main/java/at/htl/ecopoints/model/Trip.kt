package at.htl.ecopoints.model

import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class Trip(
    val id: UUID,
    val distance: Double,

    @SerializedName("avg_speed")
    val avgSpeed: Double,

    @SerializedName("avg_engine_rotation")
    val avgEngineRotation: Double,

    val date: Date,

    @SerializedName("rewarded_eco_points")
    val rewardedEcoPoints: Double,
    //val user: User
) {
    override fun toString(): String {
        return String.format(
            "Trip[id=%d, distance=%f, avgSpeed=%f, avgEngineRotation=%f, date=%s, rewardedEcoPoints=%f]",
            id,
            distance,
            avgSpeed,
            avgEngineRotation,
            date,
            rewardedEcoPoints,
            /*user*/)
    }
}
