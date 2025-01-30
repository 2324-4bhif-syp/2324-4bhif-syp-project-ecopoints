using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

public class Trip
{
    public Guid TripId { get; set; }
    public List<CarSensorData> Data { get; set; } = new List<CarSensorData>(); 
}

public class CarSensorData
{
    public DateTime Timestamp { get; set; }
    public CarData CarData { get; set; }
}

public class CarData
{
    public double? Latitude { get; set; }
    public double? Longitude { get; set; }
    public double? Altitude { get; set; }
    public double? EngineLoad { get; set; }
    public double? CoolantTemperature { get; set; }
    public double? EngineRpm { get; set; }
    public double? GpsSpeed { get; set; }
    public double? ObdSpeed { get; set; }
}
    

public class Graph
{
    public int Id { get; set; }
    public string Title { get; set; }
    public string IFrameLink { get; set; }
}
class Program
{
    private static async Task Main(string[] args)
    {
        //var tripData = GenerateFakeTripData();
        //postTripData(tripData);

        await postGraphData();
        Console.WriteLine("FINISH");

    }
    
    private static async void postTripData(Trip tripData)
    {
        using var httpClient = new HttpClient();

        
        try
        {
            var response = await httpClient.PostAsJsonAsync("http://localhost:5221/api/log", tripData);
            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine("Trip data successfully sent and logged in the database.");
            }
            else
            {
                Console.WriteLine($"Failed to log data. Status code: {response.StatusCode}");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"An error occurred: {ex.Message}");
        }
    }

    private static async Task postGraphData()
    {
        using var client = new HttpClient();
        client.BaseAddress = new Uri("https://if200210.cloud.htl-leonding.ac.at"); 

        var graphs = new[]
        {
            new Graph { Id = 1, Title = "Obd Speed", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 2, Title = "Gps Speed", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=4&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 3, Title = "Engine Rpm", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=5&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 4, Title = "Engine Load", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=7&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 5, Title = "Coolant Temp", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=6&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 6, Title = "longitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=2&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 7, Title = "latitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=3&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 8, Title = "altitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=8&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" }
        };

        foreach (var graph in graphs)
        {
            try
            {
                Console.WriteLine($"📡 Sending graph '{graph.Title}' to API...");

                var response = await client.PostAsJsonAsync("/api/graph", graph);
                response.EnsureSuccessStatusCode(); // Wirft Exception, falls nicht erfolgreich

                Console.WriteLine($"✅ Successfully posted graph '{graph.Title}'!");
            }
            catch (HttpRequestException httpEx)
            {
                Console.WriteLine($"❌ HTTP Error: {httpEx.Message}");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ General Error: {ex.Message}");
            }
        }
    }

    private static Trip GenerateFakeTripData()
    {
        var trip = new Trip
        {
            TripId = Guid.NewGuid()
        };

        var random = new Random();

        for (int i = 0; i < 10; i++)
        {
            var sensorData = new CarSensorData
            {
                Timestamp = DateTime.UtcNow.AddSeconds(-i * 10),
                CarData = new CarData 
                {
                    Latitude = random.NextDouble() * 180 - 90,
                    Longitude = random.NextDouble() * 360 - 180,
                    Altitude = random.NextDouble() * 1000,
                    EngineLoad = random.NextDouble() * 100,
                    CoolantTemperature = random.NextDouble() * 120,
                    EngineRpm = random.Next(1000, 8000),
                    GpsSpeed = random.NextDouble() * 200,
                    ObdSpeed = random.NextDouble() * 200
                }
            };

            trip.Data.Add(sensorData);
        }

        return trip;
    }

}
