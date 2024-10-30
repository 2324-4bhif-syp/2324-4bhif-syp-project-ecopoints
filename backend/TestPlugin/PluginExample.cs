using Attributes;
using Base;
using DataService.Services;

namespace TestPlugin;

[Plugin("influx_plugin_example", typeof(void))]
public class PluginExample : IBasePluginLayout
{
    private readonly InfluxDbService _dbService;

    public bool RequiresCalculation => false;

    public PluginExample(InfluxDbService dbService)
    {
        _dbService = dbService;
    }

    public async Task<IEnumerable<Dictionary<string, object>>> ExecuteQuery(QueryParameters parameters)
    {
        // Verwenden der `GetTripDataAsync` Methode, um Trip-Daten basierend auf einer Trip-ID zu erhalten
        var tripId = parameters.Ids.FirstOrDefault(); // Hier nur als Beispiel mit der ersten ID

        if (tripId == null)
            return new List<Dictionary<string, object>> { new Dictionary<string, object> { { "Error", "Trip ID is required" } } };

        var tripData = await _dbService.GetTripDataAsync(tripId);

        // Aggregation der Daten
        double sumGpsSpeed = 0;
        int count = 0;
        foreach (var data in tripData)
        {
            sumGpsSpeed += data.CarData.GpsSpeed;
            count++;
        }

        double averageGpsSpeed = count > 0 ? sumGpsSpeed / count : 0;

        return new List<Dictionary<string, object>>
        {
            new Dictionary<string, object>
            {
                { "tripId", tripId },
                { "sumGpsSpeed", sumGpsSpeed },
                { "averageGpsSpeed", averageGpsSpeed }
            }
        };
    }
}