using Abstractions.Model;
using Attributes;
using Base;
using DataService.Services;

namespace EngineHealthPlugin;

[Plugin("engineHealth", typeof(void))]
public class EngineHealth : IBasePluginLayout
{
    private readonly InfluxDbService _dbService;
    public bool RequiresCalculation => false;

    public EngineHealth(InfluxDbService dbService) => _dbService = dbService;

    public async Task<IEnumerable<Dictionary<string, object>>> ExecuteQuery(QueryParameters parameters)
    {
        var tripId = parameters.Ids.FirstOrDefault();
        if (tripId == Guid.Empty)
            return new[] { new Dictionary<string, object> { { "Error", "Trip ID required" } } };

        var data = await _dbService.GetTripDataAsync(tripId);
        
        return data.Where(d => FilterByDate(d, parameters))
            .Select(d => new Dictionary<string, object>
            {
                { "timestamp", d.Timestamp },
                { "coolantTemp", d.CarData.CoolantTemperature },
                { "overheatWarning", d.CarData.CoolantTemperature > 100 ? "WARNING" : "OK" }
            });
    }

    private bool FilterByDate(CarSensorData data, QueryParameters parameters)
        => (!parameters.StartDate.HasValue || data.Timestamp >= parameters.StartDate) &&
           (!parameters.EndDate.HasValue || data.Timestamp <= parameters.EndDate);
}