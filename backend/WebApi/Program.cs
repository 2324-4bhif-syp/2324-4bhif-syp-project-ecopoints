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
using Trip = Abstractions.Model.Trip;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
    {
        policy.WithOrigins("http://localhost:4200") 
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
    var pluginSystem = new PluginSystem(config.AllPlugins.PluginsFolderPath);
    pluginSystem.Initialize();
    return pluginSystem;
});

builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("Default")));

var app = builder.Build();

app.UseCors("AllowFrontend");

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

//Dynamicly load plugins
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

app.MapPut("/api/graph/{id:int}", (GraphController controller, int id, Graph updatedGraph) => controller.UpdateGraph(id, updatedGraph))
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
