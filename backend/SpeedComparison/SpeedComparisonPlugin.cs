using Abstractions.Model;
using Attributes;
using Base;
using DataService.Services;

namespace SpeedComparisonPlugin;

[Plugin("speedComparison", typeof(void))]
public class SpeedComparison : IBasePluginLayout
{
    private readonly InfluxDbService _dbService;
    public bool RequiresCalculation => false;

    public SpeedComparison(InfluxDbService dbService) => _dbService = dbService;

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
                { "gpsSpeed", d.CarData.GpsSpeed },
                { "obdSpeed", d.CarData.ObdSpeed },
                { "speedDifference", d.CarData.GpsSpeed - d.CarData.ObdSpeed }
            });
    }

    private bool FilterByDate(CarSensorData data, QueryParameters parameters)
        => (!parameters.StartDate.HasValue || data.Timestamp >= parameters.StartDate) &&
           (!parameters.EndDate.HasValue || data.Timestamp <= parameters.EndDate);
}