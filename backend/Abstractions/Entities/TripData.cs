using Abstractions.Model;

namespace Abstractions.Entities;

public class TripData
{
    public DateTime Timestamp { get; set; } 
    public CarData CarData { get; set; }
}