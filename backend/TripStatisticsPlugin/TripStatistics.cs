using Abstractions.Model;
using Attributes;
using Base;
using DataService.Services;

namespace TripStatisticsPlugin;

[Plugin("tripStatistics", typeof(void))]
public class TripStatistics : IBasePluginLayout
{
    private readonly InfluxDbService _dbService;

    public TripStatistics(InfluxDbService dbService) => _dbService = dbService;

    public async Task<IEnumerable<Dictionary<string, object>>> ExecuteQuery(QueryParameters parameters)
    {
        var tripId = parameters.Ids.FirstOrDefault();
        if (tripId == Guid.Empty)
            return new[] { new Dictionary<string, object> { { "Error", "Trip ID required" } } };

        var data = (await _dbService.GetTripDataAsync(tripId)).ToList();
        
        return new[] {
            new Dictionary<string, object> {
                { "maxSpeed", data.Max(d => d.CarData.GpsSpeed) },
                { "avgSpeed", data.Average(d => d.CarData.GpsSpeed) },
                { "maxRpm", data.Max(d => d.CarData.EngineRpm) },
                { "distance", CalculateDistance(data) }
            }
        };
    }

    private double CalculateDistance(IEnumerable<CarSensorData> data)
    {
        var ordered = data.OrderBy(d => d.Timestamp).ToList();
        return ordered.Zip(ordered.Skip(1), (a, b) => 
            Math.Sqrt(Math.Pow(b.CarData.Latitude - a.CarData.Latitude, 2) + 
                      Math.Pow(b.CarData.Longitude - a.CarData.Longitude, 2))).Sum();
    }
}