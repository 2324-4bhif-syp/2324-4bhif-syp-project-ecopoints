using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;

namespace Persistence.Entities;

[Table("ECO_TRIP")]
public class Trip
{
    public Guid Id { get; set; } 

    public double Distance { get; set; }

    [JsonProperty("avg_speed")]
    public double AvgSpeed { get; set; }

    [JsonProperty("avg_engine_rotation")]
    public double AvgEngineRotation { get; set; }

    public DateTime Date { get; set; }

    [JsonProperty("rewarded_eco_points")]
    public double RewardedEcoPoints { get; set; }
}