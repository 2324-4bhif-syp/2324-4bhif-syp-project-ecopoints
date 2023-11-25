package at.htl.ecopoints.model

import java.util.Date

data class Trip(
    val id: Long,
    val distance: Double,
    val avgSpeed: Double,
    val avgEngineRotation: Double,
    val data: Date,
    val rewardedEcoPoints: Double,
    val user: User
) {
    override fun toString(): String {
        return String.format(
            "Trip[id=%d, distance=%f, avgSpeed=%f, avgEngineRotation=%f, data=%s, rewardedEcoPoints=%f, user=%s]",
            id,
            distance,
            avgSpeed,
            avgEngineRotation,
            data,
            rewardedEcoPoints,
            user)
    }
}
