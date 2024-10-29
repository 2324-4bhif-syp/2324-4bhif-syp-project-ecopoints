using Newtonsoft.Json;

namespace Abstractions.Model;

public class CarData
{
    [JsonProperty("Latitude")]
    public double Latitude { get; set; }

    [JsonProperty("Longitude")]
    public double Longitude { get; set; }

    [JsonProperty("Altitude")]
    public double Altitude { get; set; }

    [JsonProperty("Engine Load")]
    public double EngineLoad { get; set; }

    [JsonProperty("Engine Coolant Temperature")]
    public double CoolantTemperature { get; set; }

    [JsonProperty("Engine RPM")]
    public double EngineRpm { get; set; }

    [JsonProperty("Gps-Speed")]
    public double GpsSpeed { get; set; }

    [JsonProperty("Vehicle Speed")]
    public double ObdSpeed { get; set; }
}