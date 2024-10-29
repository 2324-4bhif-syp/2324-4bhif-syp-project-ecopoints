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

app.MapGet("/api/CarSensor/health", async (InfluxDbService influxDbService) =>
{
    var isHealthy = await influxDbService.IsDatabaseHealthyAsync();
    return isHealthy ? Results.Ok("Database is healthy") : Results.StatusCode(500);
})
.WithOpenApi(operation =>
{
    operation.Summary = "Check if database is up and running"; 
    return operation;
});

app.MapPost("/api/CarSensor/log", async (InfluxDbService influxDbService, Trip trip) =>
{
    if (trip == null || trip.Data == null || trip.Data.Count == 0)
    {
        return Results.BadRequest("Invalid trip data");
    }

    await influxDbService.WriteTripDataAsync(trip);
    return Results.Ok("Data logged successfully");
})
.WithOpenApi(operation =>
{
    operation.Summary = "Save whole trip to database"; 
    return operation;
});

app.MapPost("/api/CarSensor/create-trip", async (InfluxDbService influxDbService) =>
{
    var tripId = await influxDbService.CreateTripAsync();
    return Results.Ok(new { TripId = tripId });
})
.WithOpenApi(operation =>
{
    operation.Summary = "Get a new tripId, which is saved empty in the database (not saved currently)";
    return operation;
});

app.MapGet("/api/CarSensor/trips", async (InfluxDbService influxDbService) =>
{
    var tripIds = await influxDbService.GetAllTripsAsync();

    if (tripIds == null || tripIds.Count == 0)
    {
        return Results.NotFound("No trips found in the database");
    }

    return Results.Ok(tripIds);
})
.WithOpenApi(operation =>
{
    operation.Summary = "Get all tripIds";
    return operation;
});

app.MapGet("/api/CarSensor/trip/{tripId:guid}", async (InfluxDbService influxDbService, Guid tripId) =>
{
    var data = await influxDbService.GetTripDataAsync(tripId);

    if (data == null || data.Count == 0)
    {
        return Results.NotFound("No data found for this trip ID");
    }

    return Results.Ok(data);
})
.WithOpenApi(operation =>
{
    operation.Summary = "Get data from a specific trip";
    return operation;
});

app.MapPost("/api/CarSensor/trip/{tripId:guid}/data", async (InfluxDbService influxDbService, Guid tripId, List<CarSensorData> sensorData) =>
{
    if (sensorData == null || sensorData.Count == 0)
    {
        return Results.BadRequest("Invalid sensor data");
    }

    await influxDbService.AddDataToSpecificTrip(tripId, sensorData);
    return Results.Ok("Data logged successfully");
})
.WithOpenApi(operation =>
{
    operation.Summary = "Add sensor data to an existing trip by ID";
    return operation;
});

app.Run();
