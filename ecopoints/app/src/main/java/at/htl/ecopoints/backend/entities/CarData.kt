import java.sql.Timestamp

data class CarData(
    val id: Long,
    val longitude: Double,
    val latitude: Double
    val currentEngineRPM: Double,
    val currentVelocity: Double,
    val throttlePosition: Double,
    val engineRunTime: String,
    val timestamp: Timestamp
    ){
    constructor(id: Long, longitude: Double,
    latitude: Double, currentEngineRPM: Double
    currentVelocity: Double, throttlePosition: Double,
    engineRunTime: String, timestamp: java.sql.Timestamp) : this(id, longitude,
        latitude, currentEngineRPM,
        currentVelocity, throttlePosition,
        engineRunTime, timestamp)
}