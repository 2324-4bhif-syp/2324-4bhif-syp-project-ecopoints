using Attributes;
using Base;
using DataService.Services;

namespace LatitudePlugin;

[Plugin("latitude", typeof(void))]
public class Latitude: IBasePluginLayout
{   
    private readonly InfluxDbService _dbService;

    public bool RequiresCalculation => false;

    public Latitude(InfluxDbService dbService)
    {
        _dbService = dbService;
    }

    public async Task<IEnumerable<Dictionary<string, object>>> ExecuteQuery(QueryParameters parameters)
    {
        var tripId = parameters.Ids.FirstOrDefault();
        if (tripId == Guid.Empty)
        {
            return new List<Dictionary<string, object>> { new Dictionary<string, object> { { "Error", "Trip ID is required" } } };
        }

        var tripData = await _dbService.GetTripDataAsync(tripId);

        Console.WriteLine($"Fetched {tripData.Count} entries for tripId {tripId}.");
        
        var filteredData = tripData
            .Where(data => (!parameters.StartDate.HasValue || data.Timestamp >= parameters.StartDate) &&
                           (!parameters.EndDate.HasValue || data.Timestamp <= parameters.EndDate))
            .Select(data => new Dictionary<string, object>
            {
                { "timestamp", data.Timestamp },
                { "latitude", data.CarData.Latitude }
            });

        return filteredData;
    }
}
