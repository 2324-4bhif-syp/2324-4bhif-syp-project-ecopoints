﻿using System;
using System.Threading.Tasks;
using Abstractions.Model;
using InfluxDB.Client;
using InfluxDB.Client.Api.Domain;
using InfluxDB.Client.Writes;
using Microsoft.Extensions.Configuration;
using InfluxDB.Client.Api.Domain; // For Bucket and RetentionRule classes
using System.Collections.Generic;   // For List<T>


namespace DataService.Services
{
    public class InfluxDbService : IDisposable
    {
        private readonly InfluxDBClient m_influxDbClient;
        private readonly string? m_bucket;
        private readonly string? m_org;

        public InfluxDbService(IConfiguration configuration)
        {
            var url = configuration["InfluxDB:Url"];
            var token = configuration["InfluxDB:Token"];
            m_org = configuration["InfluxDB:Organization"];
            m_bucket = configuration["InfluxDB:Bucket"];

            m_influxDbClient = InfluxDBClientFactory.Create(url, token.ToCharArray());
        }

        public async Task WriteTripDataAsync(Trip trip)
        {
            // Ensure the bucket exists
            await EnsureBucketExistsAsync();

            var writeApi = m_influxDbClient.GetWriteApiAsync();

            foreach (var dataPoint in trip.Data)
            {
                // Log the data point before writing to the database
                Console.WriteLine($"Writing data point: Trip ID: {trip.TripId}, Timestamp: {dataPoint.Timestamp}, Altitude: {dataPoint.CarData.Altitude}, Longitude: {dataPoint.CarData.Longitude}, Latitude: {dataPoint.CarData.Latitude}");

                var point = PointData
                    .Measurement("car_sensors_data")
                    .Tag("trip-id", trip.TripId.ToString())
                    .Field("Altitude", dataPoint.CarData.Altitude)
                    .Field("Longitude", dataPoint.CarData.Longitude)
                    .Field("Latitude", dataPoint.CarData.Latitude)
                    .Field("CoolantTemperature", dataPoint.CarData.CoolantTemperature)
                    .Field("EngineLoad", dataPoint.CarData.EngineLoad)
                    .Field("EngineRpm", dataPoint.CarData.EngineRpm)
                    .Field("GpsSpeed", dataPoint.CarData.GpsSpeed)
                    .Field("ObdSpeed", dataPoint.CarData.ObdSpeed)
                    .Timestamp(dataPoint.Timestamp, WritePrecision.Ms);

                Console.WriteLine(point);

                await writeApi.WritePointAsync(point, m_bucket, m_org);
            }
        }

        private async Task<string> GetOrganizationIdAsync(string orgName)
        {
            var orgsApi = m_influxDbClient.GetOrganizationsApi();
            var orgs = await orgsApi.FindOrganizationsAsync();

            var organization = orgs.FirstOrDefault(o => o.Name.Equals(orgName, StringComparison.OrdinalIgnoreCase));

            return organization?.Id;
        }


        private async Task EnsureBucketExistsAsync()
        {
            var bucketsApi = m_influxDbClient.GetBucketsApi();
            var buckets = await bucketsApi.FindBucketsAsync();
            bool bucketExists = buckets.Any(b => b.Name.Equals(m_bucket, StringComparison.OrdinalIgnoreCase));

            if (bucketExists)
            {
                Console.WriteLine($"Bucket '{m_bucket}' already exists.");
                return; 
            }

            Console.WriteLine($"Creating bucket '{m_bucket}'...");

            var retentionRule = new BucketRetentionRules(BucketRetentionRules.TypeEnum.Expire, everySeconds:0);
            var bucket = new Bucket(name: m_bucket, retentionRules: new List<BucketRetentionRules> { retentionRule }, orgID: await GetOrganizationIdAsync(m_org));

            await bucketsApi.CreateBucketAsync(bucket);
            Console.WriteLine($"Bucket '{m_bucket}' created successfully.");
        }


        public async Task<bool> IsDatabaseHealthyAsync()
        {
            try
            {
                var health = await m_influxDbClient.HealthAsync();
                return health.Status == HealthCheck.StatusEnum.Pass;
            }
            catch
            {
                return false;
            }
        }

        public async Task<List<CarSensorData>> GetTripDataAsync(Guid tripId)
        {
            var query = $"from(bucket: \"{m_bucket}\") " +
                        $"|> range(start: -30d) " + // Ensure this range is correct
                        $"|> filter(fn: (r) => r[\"trip-id\"] == \"{tripId}\") " +
                        $"|> filter(fn: (r) => r._measurement == \"car_sensors_data\") " + // Make sure to filter by the correct measurement
                        $"|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")"; // Pivot to get fields as columns

            var queryApi = m_influxDbClient.GetQueryApi();
            var tables = await queryApi.QueryAsync(query, m_org);

            var results = new List<CarSensorData>();

            foreach (var table in tables)
            {
                foreach (var record in table.Records)
                {
                    Console.WriteLine($"Record: {record}");

                    var carSensorData = new CarSensorData
                    {
                        Timestamp = record.GetTime()?.ToDateTimeUtc() ?? DateTime.UtcNow,
                        CarData = new CarData
                        {
                            Latitude = record.GetValueByKey("Latitude") as double? ?? 0,
                            Longitude = record.GetValueByKey("Longitude") as double? ?? 0,
                            Altitude = record.GetValueByKey("Altitude") as double? ?? 0,
                            CoolantTemperature = record.GetValueByKey("CoolantTemperature") as double? ?? 0,
                            EngineLoad = record.GetValueByKey("EngineLoad") as double? ?? 0,
                            EngineRpm = record.GetValueByKey("EngineRpm") as double? ?? 0,
                            GpsSpeed = record.GetValueByKey("GpsSpeed") as double? ?? 0,
                            ObdSpeed = record.GetValueByKey("ObdSpeed") as double? ?? 0,
                        }
                    };

                    results.Add(carSensorData);
                }
            }

            return results;
        }



        public async Task<List<Guid>> GetAllTripsAsync()
        {
            var query = $"import \"influxdata/influxdb/schema\"\n" +
                        $"schema.tagValues(bucket: \"{m_bucket}\", tag: \"trip-id\")";

            var queryApi = m_influxDbClient.GetQueryApi();
            var tables = await queryApi.QueryAsync(query, m_org);

            var tripIds = new List<Guid>();

            foreach (var table in tables)
            {
                foreach (var record in table.Records)
                {
                    if (Guid.TryParse(record.GetValue().ToString(), out var tripId))
                    {
                        tripIds.Add(tripId);
                    }
                }
            }

            return tripIds;
        }

        public void Dispose()
        {
            m_influxDbClient.Dispose();
        }
    }
}
