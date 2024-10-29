using DataService.Services;
using Abstractions.Model;
using DataService.Controller;
using Microsoft.Extensions.Configuration;

var builder = WebApplication.CreateBuilder(args);
builder.Configuration.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<InfluxDbService>();
builder.Services.AddSingleton<SensorDataController>();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.MapGet("/api/health", (SensorDataController controller) => controller.CheckHealth())
    .WithOpenApi(operation =>
    {
        operation.Summary = "Check if database is up and running";
        return operation;
    }); 

app.MapPost("/api/log", (SensorDataController controller, Trip trip) => controller.LogData(trip))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Save whole trip to database";
        return operation;
    }); 

app.MapPost("/api/create-trip", (SensorDataController controller) => controller.CreateTrip())
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get a new tripId, which is saved empty in the database (not saved currently)";
        return operation;
    });

app.MapGet("/api/trips", (SensorDataController controller) => controller.GetAllTrips())
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get all tripIds";
        return operation;
    });

app.MapGet("/api/trip/{tripId:guid}", (SensorDataController controller, Guid tripId) => controller.GetTripData(tripId))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get data from a specific trip";
        return operation;
    });

app.MapPost("/api/trip/{tripId:guid}/data", (SensorDataController controller, Guid tripId, List<CarSensorData> sensorData) => controller.AddDataToTrip(tripId, sensorData))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Add sensor data to an existing trip by ID";
        return operation;
    });

app.Run();
