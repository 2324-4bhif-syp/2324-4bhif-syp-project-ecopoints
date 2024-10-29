using DataService.Services;
using Abstractions.Model;
using Microsoft.Extensions.Configuration;

var builder = WebApplication.CreateBuilder(args);
builder.Configuration.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<InfluxDbService>();


var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

// HealthCheck Endpoint
app.MapGet("/api/CarSensor/health", async (InfluxDbService influxDbService) =>
{
    var isHealthy = await influxDbService.IsDatabaseHealthyAsync();
    return isHealthy ? Results.Ok("Database is healthy") : Results.StatusCode(500);
});

// Post Endpoint to add trip data
app.MapPost("/api/CarSensor/log", async (InfluxDbService influxDbService, Trip trip) =>
{
    if (trip == null || trip.Data == null || trip.Data.Count == 0)
    {
        return Results.BadRequest("Invalid trip data");
    }

    await influxDbService.WriteTripDataAsync(trip);
    return Results.Ok("Data logged successfully");
});

// GetAllTrips Endpoint to retrieve all unique trips
app.MapGet("/api/CarSensor/trips", async (InfluxDbService influxDbService) =>
{
    var tripIds = await influxDbService.GetAllTripsAsync();

    if (tripIds == null || tripIds.Count == 0)
    {
        return Results.NotFound("No trips found in the database");
    }

    return Results.Ok(tripIds);
});

// GetAll Endpoint to retrieve data for a specific trip ID
app.MapGet("/api/CarSensor/trip/{tripId:guid}", async (InfluxDbService influxDbService, Guid tripId) =>
{
    var data = await influxDbService.GetTripDataAsync(tripId);

    if (data == null || data.Count == 0)
    {
        return Results.NotFound("No data found for this trip ID");
    }

    return Results.Ok(data);
});

app.Run();