package at.htl.ecopoints.model

import java.util.Date

data class Trip(
    val id: Long,
    val distance: Double,
    val avgSpeed: Double,
    val avgEngineRotation: Double,
    val date: Date,
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
