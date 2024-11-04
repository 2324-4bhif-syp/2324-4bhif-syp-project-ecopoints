using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

public class Trip
{
    public Guid TripId { get; set; }
    public List<CarSensorData> Data { get; set; }
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

class Program
{
    private static async Task Main(string[] args)
    {
        using var httpClient = new HttpClient();
        var tripData = GenerateFakeTripData();

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
                Timestamp = DateTime.UtcNow.AddSeconds(-i * 10), // Timestamps im Abstand von 10 Sekunden
                CarData = new CarData
                {
                    Latitude = random.NextDouble() * 180 - 90, // zufällige Latitude
                    Longitude = random.NextDouble() * 360 - 180, // zufällige Longitude
                    Altitude = random.NextDouble() * 1000, // zufällige Höhe
                    EngineLoad = random.NextDouble() * 100, // Motorlast (0-100%)
                    CoolantTemperature = random.NextDouble() * 120, // Kühlmitteltemperatur (0-120 °C)
                    EngineRpm = random.Next(1000, 8000), // Motor-Drehzahl (1000-8000 RPM)
                    GpsSpeed = random.NextDouble() * 200, // GPS-Geschwindigkeit (0-200 km/h)
                    ObdSpeed = random.NextDouble() * 200 // Fahrzeug-Geschwindigkeit (0-200 km/h)
                }
            };

            trip.Data.Add(sensorData);
        }

        return trip;
    }
}
