using System.Configuration;
using Abstractions.Entities;
using DataService.Services;
using Abstractions.Model;
using Base;
using DataService.Controller;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Persistence;
using WebApi;
using Microsoft.AspNetCore.HttpOverrides;
using Microsoft.AspNetCore.Mvc; // For Forwarded Headers
using Trip = Abstractions.Model.Trip;

var builder = WebApplication.CreateBuilder(args);

// Set up CORS policy
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
    {
        policy.WithOrigins(
            builder.Configuration["AllowedOrigin"] ?? "http://localhost:4200" // Default for local dev
        )
        .AllowAnyMethod()
        .AllowAnyHeader();
    });
});

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<InfluxDbService>();
builder.Services.AddSingleton<SensorDataController>();
builder.Services.AddScoped<GraphController>();
builder.Services.AddDbContext<ApplicationDbContext>();

builder.Services.AddSingleton<PluginSystem>(provider =>
{
    var config = provider.GetRequiredService<IConfiguration>().GetSection("App").Get<AppConfig>();
    if (config == null || config.AllPlugins == null || string.IsNullOrEmpty(config.AllPlugins.PluginsFolderPath))
    {
        throw new InvalidOperationException("Configuration for plugins is missing.");
    }
    var pluginSystem = new PluginSystem(config.AllPlugins.PluginsFolderPath);
    pluginSystem.Initialize();
    return pluginSystem;
});

builder.Configuration
    .AddJsonFile("appsettings.json", optional: false, reloadOnChange: false);

builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("Default")));

var app = builder.Build();

// Use CORS
app.UseCors("AllowFrontend");

app.UseForwardedHeaders(new ForwardedHeadersOptions
{
    ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto,
    ForwardLimit = 1
});


// Enable Swagger in development mode
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Default route to avoid redirect loops or errors
//app.MapFallback(() => Results.Redirect("/swagger"));

// Add middleware for request logging
app.Use(async (context, next) =>
{
    Console.WriteLine($"Request Path: {context.Request.Path}");
    Console.WriteLine($"Request Headers: {string.Join(", ", context.Request.Headers.Select(h => $"{h.Key}: {h.Value}"))}");
    await next();
});

// Map routes
app.MapGet("/api/{pluginName}", async (string pluginName, PluginSystem pluginSystem, InfluxDbService dbService, HttpContext context) =>
{
    var pluginType = pluginSystem.FindPlugin(pluginName);
    if (pluginType == null)
        return Results.NotFound($"Plugin {pluginName} not found.");

    var pluginInstance = Activator.CreateInstance(pluginType, dbService) as IBasePluginLayout;
    if (pluginInstance == null)
        return Results.BadRequest("Could not create plugin instance.");

    var startDate = context.Request.Query.ContainsKey("startDate")
        ? DateTime.Parse(context.Request.Query["startDate"])
        : (DateTime?)null;

    var endDate = context.Request.Query.ContainsKey("endDate")
        ? DateTime.Parse(context.Request.Query["endDate"])
        : (DateTime?)null;

    var ids = context.Request.Query.ContainsKey("ids")
        ? context.Request.Query["ids"].ToString().Split(',')
            .Where(id => Guid.TryParse(id, out _))
            .Select(id => Guid.Parse(id))
            .ToList()
        : new List<Guid>();

    var parameters = new QueryParameters
    {
        StartDate = startDate,
        EndDate = endDate,
        Ids = ids
    };

    var result = await pluginInstance.ExecuteQuery(parameters);
    return Results.Ok(result);
});


// SensorDataController endpoints
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

app.MapGet("/api/version", () => Results.Ok("1.0.0"))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get the version of the API";
        return operation;
    });

app.MapPost("/api/log/token", async (SensorDataController controller, Trip trip, [FromQuery] string token) =>
    {
        // Optionally: Validate the token or process it here
        return await controller.LogDataToken(trip, token);
    })
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

app.MapGet("/api/tripIds", (SensorDataController controller) => controller.GetAllTripIds())
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

app.MapGet("/api/trips", (SensorDataController controller) => controller.GetTripsData())
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get calculated metadata from a specific trip";
        return operation;
    });

app.MapPost("/api/trip/{tripId:guid}/data", (SensorDataController controller, Guid tripId, List<CarSensorData> sensorData) => controller.AddDataToTrip(tripId, sensorData))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Add sensor data to an existing trip by ID";
        return operation;
    });

// GraphController endpoints
app.MapGet("/api/graphs", (GraphController controller) => controller.GetAllGraphs())
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get all graphs";
        return operation;
    });

app.MapGet("/api/graph/{id:int}", (GraphController controller, int id) => controller.GetGraphById(id))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Get a graph by ID";
        return operation;
    });

app.MapPost("/api/graph", (GraphController controller, Graph graph) => controller.AddGraph(graph))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Add a new graph";
        return operation;
    });

app.MapPut("/api/graph/{id:int}", async (GraphController controller, int id, [FromBody] Graph updatedGraph) =>
    {
        return await controller.UpdateGraph(id, updatedGraph, updatedGraph.RequiresCalc);
    })
    .WithOpenApi(operation =>
    {
        operation.Summary = "Update an existing graph";
        return operation;
    });


app.MapDelete("/api/graph/{id:int}", (GraphController controller, int id) => controller.DeleteGraph(id))
    .WithOpenApi(operation =>
    {
        operation.Summary = "Delete a graph by ID";
        return operation;
    });

app.Run();
