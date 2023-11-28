package at.htl.ecopoints.db


import java.sql.Timestamp

public data class CarData(
    val id: Long,
    val longitude: Double,
    val latitude: Double,
    val currentEngineRPM: Double,
    val currentVelocity: Double,
    val throttlePosition: Double,
    val engineRunTime: String,
    val timestamp: Timestamp
    ){
}