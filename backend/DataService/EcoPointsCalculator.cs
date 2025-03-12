using System;
using System.Collections.Generic;
using System.Linq;
using Abstractions.Entities;
using Abstractions.Model;
using Newtonsoft.Json;

namespace DataService
{
    internal static class EcoPointsCalculator
    {
        // Thresholds for driving behavior
        private const double HARSH_ACCELERATION_THRESHOLD = 3.0;  // m/s²
        private const double HARSH_BRAKING_THRESHOLD = -4.0;      // m/s²
        private const double HIGH_SPEED_THRESHOLD = 120.0;        // km/h
        private const int HIGH_RPM_THRESHOLD = 4000;             // RPM
        private const double HIGH_ENGINE_LOAD_THRESHOLD = 75.0;   // %

        // Penalty values
        private const int HARSH_ACCELERATION_PENALTY = 2;
        private const int HARSH_BRAKING_PENALTY = 3;
        private const int HIGH_SPEED_PENALTY = 5;
        private const int HIGH_RPM_PENALTY = 2;
        private const int HIGH_ENGINE_LOAD_PENALTY = 10;

        public static EcoPointsMetaData CalculateEcoPoints(IEnumerable<CarSensorData> data)
        {
            if (data == null || !data.Any())
                return new EcoPointsMetaData { EcoPoints = 0 }; // No data means no score.

            var metadata = new EcoPointsMetaData();
            int ecoScore = 100; // Start with a perfect score

            var sortedData = data.OrderBy(d => d.Timestamp).ToList();

            double totalEngineLoad = 0;
            int validLoadCount = 0;

            for (int i = 1; i < sortedData.Count; i++)
            {
                var previous = sortedData[i - 1];
                var current = sortedData[i];

                var timeDiff = (current.Timestamp - previous.Timestamp).TotalSeconds;
                if (timeDiff <= 0) continue; // Skip invalid timestamps.

                // Calculate acceleration (m/s²)
                double speedDiff = (current.CarData.GpsSpeed - previous.CarData.GpsSpeed);
                double acceleration = speedDiff / timeDiff;

                // Detect harsh acceleration/braking
                if (acceleration > HARSH_ACCELERATION_THRESHOLD) metadata.HarshAccelerationCount++;
                if (acceleration < HARSH_BRAKING_THRESHOLD) metadata.HarshBrakingCount++;

                // Check for high-speed driving
                if (current.CarData.GpsSpeed > HIGH_SPEED_THRESHOLD) metadata.HighSpeedCount++;

                // Check for high RPM
                if (current.CarData.EngineRpm > HIGH_RPM_THRESHOLD) metadata.HighRpmCount++;

                // Engine Load tracking
                // if (current.CarData.EngineLoad > 0)
                // {
                    totalEngineLoad += current.CarData.EngineLoad;
                    validLoadCount++;
                // }
            }

            // Apply penalties
            ecoScore -= (metadata.HarshAccelerationCount * HARSH_ACCELERATION_PENALTY);
            ecoScore -= (metadata.HarshBrakingCount * HARSH_BRAKING_PENALTY);
            ecoScore -= (metadata.HighSpeedCount * HIGH_SPEED_PENALTY);
            ecoScore -= (metadata.HighRpmCount * HIGH_RPM_PENALTY);

            // Penalize inefficient engine load
            if (validLoadCount > 0)
            {
                metadata.AverageEngineLoad = totalEngineLoad / validLoadCount;
                if (metadata.AverageEngineLoad > HIGH_ENGINE_LOAD_THRESHOLD) ecoScore -= HIGH_ENGINE_LOAD_PENALTY;
            }

            metadata.EcoPoints = Math.Max(ecoScore, 0);
            return metadata;
        }
    }
}