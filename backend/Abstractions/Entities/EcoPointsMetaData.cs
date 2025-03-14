namespace Abstractions.Entities
{
    public class EcoPointsMetaData
    {
        public int HarshAccelerationCount { get; set; }
        public int HarshBrakingCount { get; set; }
        public int HighSpeedCount { get; set; }
        public int HighRpmCount { get; set; }
        public double AverageEngineLoad { get; set; }
        public int EcoPoints { get; set; }
    }
}