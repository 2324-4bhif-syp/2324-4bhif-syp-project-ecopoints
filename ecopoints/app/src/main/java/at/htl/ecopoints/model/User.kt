package at.htl.ecopoints.model

data class User(
    val id: Long,
    val userName: String,
    val password: String,
    val ecoPoints: Double
)
