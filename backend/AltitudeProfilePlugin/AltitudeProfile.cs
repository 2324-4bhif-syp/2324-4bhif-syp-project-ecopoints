using Abstractions.Model;
using Attributes;
using Base;
using DataService.Services;

namespace AltitudePlugin;

[Plugin("altitudeProfile", typeof(void))]
public class AltitudeProfile : IBasePluginLayout
{
    private readonly InfluxDbService _dbService;
    public bool RequiresCalculation => false;

    public AltitudeProfile(InfluxDbService dbService) => _dbService = dbService;

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
                { "altitude", d.CarData.Altitude }
            });
    }

    private bool FilterByDate(CarSensorData data, QueryParameters parameters)
        => (!parameters.StartDate.HasValue || data.Timestamp >= parameters.StartDate) &&
           (!parameters.EndDate.HasValue || data.Timestamp <= parameters.EndDate);
}