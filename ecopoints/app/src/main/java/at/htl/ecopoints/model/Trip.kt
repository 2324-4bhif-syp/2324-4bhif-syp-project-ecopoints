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
)
