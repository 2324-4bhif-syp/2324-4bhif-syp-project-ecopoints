using Abstractions.Model;
using DataService.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;


namespace DataService.Controller
{
    public class SensorDataController(InfluxDbService dbService)
    {
        private readonly InfluxDbService _dbService = dbService;

        public async Task<IResult> CheckHealth()
        {
            var isHealthy = await _dbService.IsDatabaseHealthyAsync();
            return isHealthy ? Results.Ok("Database is healthy") : Results.StatusCode(500);
        }

        public async Task<IResult> LogData(Trip trip)
        {   
            if (trip?.Data == null || trip.Data.Count == 0)
            {
                return Results.BadRequest("Invalid trip data");
            }

            await _dbService.WriteTripDataAsync(trip);
            return Results.Ok("Data logged successfully");
        }

        public async Task<IResult> LogDataToken(Trip trip, string token)
        {
            if (trip?.Data == null || trip.Data.Count == 0)
            {
                return Results.BadRequest("Invalid trip data");
            }

            await _dbService.WriteTripDataAsyncWithToken(trip, token);
            return Results.Ok("Data logged successfully");
        }

        public async Task<IResult> CreateTrip()
        {
            var tripId = await _dbService.CreateTripAsync();
            return Results.Ok(new { TripId = tripId });
        }

        public async Task<IResult> GetAllTripIds()
        {
            var tripIds = await _dbService.GetAllTripsAsync();
            return tripIds.Count == 0 ? Results.NotFound("No trips found in the database") : Results.Ok(tripIds);
        }

        public async Task<IResult> GetTripData(Guid tripId)
        {
            var data = await _dbService.GetTripDataAsync(tripId);
            return data.Count == 0 ? Results.NotFound("No data found for this trip ID") : Results.Ok(data);
        }

        public async Task<IResult> AddDataToTrip(Guid tripId, List<CarSensorData> sensorData)
        {
            if (sensorData == null || sensorData.Count == 0)
            {
                return Results.BadRequest("Invalid sensor data");
            }

            await _dbService.AddDataToSpecificTrip(tripId, sensorData);
            return Results.Ok("Data logged successfully");
        }

        public async Task<IResult> GetTripsData()
        {
            var tripIds = await _dbService.GetAllTripsAsync();
            //var dataTasks = tripIds.Select(async i => await _dbService.GetTripDataAsync(i));

            var dataTasks = await Task.WhenAll(tripIds.Select(async i =>
                new KeyValuePair<Guid, IEnumerable<CarSensorData>>(i, await _dbService.GetTripDataAsync(i))));

            
            return Results.Ok(dataTasks.Select((d) => TripMetaDataFactory.CreateFromSensorData(d.Key,d.Value)));
        }
    }
}