using Newtonsoft.Json;

namespace Abstractions.Entities.DTO;

public record CarDataEntry(
    double Longitude,
    double Latitude,
        
    [property: JsonProperty("current_engine_rpm")]
    double CurrentEngineRPM,
        
    [property: JsonProperty("current_velocity")]
    double CurrentVelocity,
        
    [property: JsonProperty("throttle_position")]
    double ThrottlePosition,
        
    [property: JsonProperty("engine_run_time")]
    string EngineRunTime,
        
    [property: JsonProperty("time_stamp")]
    DateTime TimeStamp,
        
    [property: JsonProperty("trip_id")]
    Guid TripId
);