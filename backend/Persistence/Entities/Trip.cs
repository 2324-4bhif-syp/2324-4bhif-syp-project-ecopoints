using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;

namespace Persistence.Entities;

[Table("ECO_TRIP")]
public class Trip
{
    public Guid TripId { get; set; } = Guid.NewGuid();
    public List<TripData> Data { get; set; } = new List<TripData>();
}