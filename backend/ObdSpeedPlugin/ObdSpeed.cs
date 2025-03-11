using Attributes;
using Base;
using DataService.Services;

namespace ObdSpeedPlugin;

[Plugin("obdSpeed", typeof(void))]
public class ObdSpeed : IBasePluginLayout
{   
    private readonly InfluxDbService _dbService;

    public bool RequiresCalculation => false;

    public ObdSpeed(InfluxDbService dbService)
    {
        _dbService = dbService;
    }

    public async Task<IEnumerable<Dictionary<string, object>>> ExecuteQuery(QueryParameters parameters)
    {
        var tripId = parameters.Ids.FirstOrDefault();
        if (tripId == Guid.Empty)
        {
            Console.WriteLine("⚠ Trip ID is required but was not provided!");
            return new List<Dictionary<string, object>> { new Dictionary<string, object> { { "Error", "Trip ID is required" } } };
        }

        var tripData = await _dbService.GetTripDataAsync(tripId);

        Console.WriteLine($"📊 ObdSpeedPlugin received {tripData.Count} entries for tripId {tripId}.");
        
        var filteredData = tripData
            .Where(data => (!parameters.StartDate.HasValue || data.Timestamp >= parameters.StartDate) &&
                           (!parameters.EndDate.HasValue || data.Timestamp <= parameters.EndDate))
            .Select(data => new Dictionary<string, object>
            {
                { "timestamp", data.Timestamp },
                { "obdSpeed", data.CarData.ObdSpeed }
            });

        Console.WriteLine($"✅ Returning {filteredData.Count()} ObdSpeed records for tripId {tripId}");
        
        return filteredData;
    }
}