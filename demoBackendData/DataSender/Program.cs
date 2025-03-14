using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using DataSender.Entities;
using Newtonsoft.Json;
using JsonSerializer = System.Text.Json.JsonSerializer;


class Program
{
    private static HttpClient _httpClient = new HttpClient();

    private static string filePath =
        Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "..", "..", "..", "Data", "data.json");

    private static async Task Main(string[] args)
    {
        string apiUrl = "http://localhost:5221/api/log"; // Hier API URL anpassen
        try
        {
            Console.WriteLine($"Lese Daten aus {filePath}...");
            string json = await File.ReadAllTextAsync(filePath);

            var trip = JsonConvert.DeserializeObject<Trip>(json);

            if (trip == null || trip.Data == null || trip.Data.Count == 0)
            {
                Console.WriteLine("Keine gültigen Daten im JSON gefunden.");
                return;
            }

            Console.WriteLine($"Sende {trip.Data.Count} Datenpunkte für Trip ID: {trip.TripId} an API...");

            // Daten per HTTP POST senden
            HttpResponseMessage response = await _httpClient.PostAsJsonAsync(apiUrl, trip);

            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine("Daten erfolgreich gesendet!");
            }
            else
            {
                Console.WriteLine(
                    $"Fehler beim Senden: {response.StatusCode} - {await response.Content.ReadAsStringAsync()}");
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
        
    }
}