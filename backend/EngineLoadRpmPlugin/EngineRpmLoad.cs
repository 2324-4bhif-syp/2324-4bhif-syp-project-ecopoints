using Abstractions.Model;
using Attributes;
using Base;
using DataService.Services;

namespace EngineLoadPlugin;

[Plugin("engineLoadRpm", typeof(void))] // um Fahrverhalten zu analysieren (z. B. hohe Last bei niedrigen RPM = ineffizient).
public class EngineRpmLoad : IBasePluginLayout
{
    private readonly InfluxDbService _dbService;
    public bool RequiresCalculation => false;

    public EngineRpmLoad(InfluxDbService dbService) => _dbService = dbService;

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
                { "engineLoad", d.CarData.EngineLoad },
                { "engineRpm", d.CarData.EngineRpm }
            });
    }
    private bool FilterByDate(CarSensorData data, QueryParameters parameters)
        => (!parameters.StartDate.HasValue || data.Timestamp >= parameters.StartDate) &&
           (!parameters.EndDate.HasValue || data.Timestamp <= parameters.EndDate);
}