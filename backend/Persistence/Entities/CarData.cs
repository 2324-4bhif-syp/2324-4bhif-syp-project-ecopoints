using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;

namespace Persistence.Entities;

public class CarData
{
    [JsonProperty("Latitude")]
    public long Latitude { get; set; }

    [JsonProperty("Longitude")]
    public long Longitude { get; set; }

    [JsonProperty("Altitude")]
    public long Altitude { get; set; }

    [JsonProperty("Engine Load")]
    public long EngineLoad { get; set; }

    [JsonProperty("Engine Coolant Temperature")]
    public long CoolantTemperature { get; set; }

    [JsonProperty("Engine RPM")]
    public long EngineRpm { get; set; }

    [JsonProperty("Gps-Speed")]
    public long GpsSpeed { get; set; }

    [JsonProperty("Vehicle Speed")]
    public long ObdSpeed { get; set; }
}