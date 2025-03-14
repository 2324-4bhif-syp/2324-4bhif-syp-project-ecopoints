using Abstractions.Entities.DTO;
using Abstractions.Model;

namespace DataService;

public static class TripMetaDataFactory
{
    public static TripMetaData CreateFromSensorData(Guid tripId, IEnumerable<CarSensorData> sensorData)
    {
        if (sensorData == null || !sensorData.Any())
            throw new ArgumentException("Sensor data list cannot be empty.");

        var sortedData = sensorData.OrderBy(d => d.Timestamp).ToList();

        var startDate = sortedData.First().Timestamp;
        var endDate = sortedData.Last().Timestamp;
        var duration = endDate - startDate;

        var latLngList = sortedData
            .Select(d => new Tuple<double, double>(d.CarData.Latitude, d.CarData.Longitude))
            .ToList();
        
        var avgSpeedGps = sortedData
            .Where(d => d.CarData.GpsSpeed >= GlobalConstants.MinSpeed && d.CarData.GpsSpeed <= GlobalConstants.MaxSpeed)
            .Select(d => d.CarData.GpsSpeed)
            .DefaultIfEmpty(0)
            .Average(); 
        var avgSpeedObd = sortedData
            .Where(d => d.CarData.ObdSpeed >= GlobalConstants.MinSpeed && d.CarData.ObdSpeed <= GlobalConstants.MaxSpeed)
            .Select(d => d.CarData.ObdSpeed)
            .DefaultIfEmpty(0)
            .Average();

        var maxSpeedGps = sortedData
            .Where(d => d.CarData.GpsSpeed >= GlobalConstants.MinSpeed && d.CarData.GpsSpeed <= GlobalConstants.MaxSpeed)
            .Select(d => d.CarData.GpsSpeed)
            .DefaultIfEmpty(0)
            .Max();
        
        var maxSpeedObd = sortedData
            .Where(d => d.CarData.ObdSpeed >= GlobalConstants.MinSpeed && d.CarData.ObdSpeed <= GlobalConstants.MaxSpeed)
            .Select(d => d.CarData.ObdSpeed)
            .DefaultIfEmpty(0)
            .Max();

        var avgRpm = sortedData
            .Where(d => d.CarData.EngineRpm >= GlobalConstants.MinRpm && d.CarData.EngineRpm <= GlobalConstants.MaxRpm)
            .Select(d => d.CarData.EngineRpm)
            .DefaultIfEmpty(0)
            .Average();
        
        var avgEngineLoad = sortedData
            .Where(d => d.CarData.EngineLoad >= 0 && d.CarData.EngineLoad <= 100) //Percent 0-100
            .Select(d => d.CarData.EngineLoad)
            .DefaultIfEmpty(0)
            .Average();
        
        var distance = CalculateTotalDistance(sortedData);

        var ecoPoints = EcoPointsCalculator.CalculateEcoPoints(sortedData);

        return new TripMetaData
        {
            AverageEngineLoad = avgEngineLoad,
            EcoPointsMetaData = ecoPoints,
            StartDate = startDate,
            EndDate = endDate,
            Duration = duration,
            TripId = tripId,
            Distance = distance,
            AverageSpeedGps = avgSpeedGps,
            MaxSpeedGps = maxSpeedGps,
            MaxSpeedObd = maxSpeedObd,
            AverageSpeedObd = avgSpeedObd,
            AverageRpm = avgRpm,
            LatLngList = latLngList
        };
    }

    private static double CalculateTotalDistance(List<CarSensorData> sensorData)
    {
        double totalDistance = 0.0;

        for (int i = 1; i < sensorData.Count; i++)
        {
            totalDistance += HaversineDistance(
                sensorData[i - 1].CarData.Latitude, sensorData[i - 1].CarData.Longitude,
                sensorData[i].CarData.Latitude, sensorData[i].CarData.Longitude);
        }

        return totalDistance;
    }

    private static double HaversineDistance(double lat1, double lon1, double lat2, double lon2)
    {
        const double R = 6371.0; // Radius of the Earth in km
        double dLat = ToRadians(lat2 - lat1);
        double dLon = ToRadians(lon2 - lon1);

        double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                   Math.Cos(ToRadians(lat1)) * Math.Cos(ToRadians(lat2)) *
                   Math.Sin(dLon / 2) * Math.Sin(dLon / 2);

        double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
        return R * c;
    }

    private static double ToRadians(double degrees) => degrees * (Math.PI / 180);
}