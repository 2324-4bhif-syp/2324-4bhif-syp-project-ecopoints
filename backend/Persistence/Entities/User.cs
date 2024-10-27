using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;

namespace Persistence.Entities;

[Table("ECO_TRIP")]
public class User : BaseEntity
{
    
    [JsonProperty("user_name")]
    private String userName;
    private String password;

    [JsonProperty("eco_points")]
    private double ecoPoints;
    
}