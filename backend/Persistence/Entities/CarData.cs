using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;

namespace Persistence.Entities;

[Table("ECO_CAR-DATA")]
public class CarData : BaseEntity
{
    public double Longitude { get; set; }
    public double Latitude { get; set; }

    [JsonProperty("current_engine_rpm")]
    public double CurrentEngineRPM { get; set; }

    [JsonProperty("current_velocity")]
    public double CurrentVelocity { get; set; }

    [JsonProperty("throttle_position")]
    public double ThrottlePosition { get; set; }

    [JsonProperty("engine_run_time")]
    public string EngineRunTime { get; set; }

    [JsonProperty("time_stamp")]
    public DateTime TimeStamp { get; set; }
}