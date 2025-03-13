using Abstractions.Entities;
using Abstractions.Model;

namespace DataService
{
    internal static class EcoPointsCalculator
    {
        public static EcoPointsMetaData CalculateEcoPoints(IEnumerable<CarSensorData> data)
        {
            var carSensorDatas = data.ToList();
            if (!carSensorDatas.Any())
                return new EcoPointsMetaData { EcoPoints = 0 }; 

            var metadata = new EcoPointsMetaData();
            int ecoScore = 100; // Start with a perfect score

            var sortedData = carSensorDatas.OrderBy(d => d.Timestamp).ToList();

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
                if (acceleration > GlobalConstants.HarshAccelerationThreshold) metadata.HarshAccelerationCount++; 
                if (acceleration < GlobalConstants.HarshBrakingThreshold) metadata.HarshBrakingCount++;

                // Check for high-speed driving
                if (current.CarData.GpsSpeed > GlobalConstants.HighSpeedThreshold) metadata.HighSpeedCount++;

                // Check for high RPM
                if (current.CarData.EngineRpm > GlobalConstants.HighRpmThreshold) metadata.HighRpmCount++;

                // Engine Load tracking (apply threshold filtering)
                if (current.CarData.EngineLoad is >= GlobalConstants.MinEngineLoad and <= GlobalConstants.MaxEngineLoad)
                {
                    totalEngineLoad += current.CarData.EngineLoad;
                    validLoadCount++;
                }
            }

            // Apply penalties
            ecoScore -= (metadata.HarshAccelerationCount * GlobalConstants.HarshAccelerationPenalty);
            ecoScore -= (metadata.HarshBrakingCount * GlobalConstants.HarshBrakingPenalty);
            ecoScore -= (metadata.HighSpeedCount * GlobalConstants.HighSpeedPenalty);
            ecoScore -= (metadata.HighRpmCount * GlobalConstants.HighRpmPenalty);

            // Penalize inefficient engine load
            if (validLoadCount > 0)
            {
                metadata.AverageEngineLoad = totalEngineLoad / validLoadCount;
                if (metadata.AverageEngineLoad > GlobalConstants.HighEngineLoadThreshold) 
                    ecoScore -= GlobalConstants.HighEngineLoadPenalty;
            }
            else
            {
                metadata.AverageEngineLoad = 0; // No valid engine load readings
            }

            metadata.EcoPoints = Math.Max(ecoScore, 0); // Ensure score is not negative
            return metadata;
        }
    }
}
