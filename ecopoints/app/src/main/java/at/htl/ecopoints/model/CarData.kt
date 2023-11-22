package at.htl.ecopoints.model

import java.util.UUID

data class CarData(
    val id: Long,
    val tripId: UUID,
    val longitude: Double,
    val latitude: Double,
)
