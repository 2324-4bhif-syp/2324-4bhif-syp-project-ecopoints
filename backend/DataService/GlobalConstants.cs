namespace DataService;

public static class GlobalConstants
{
    // Speed thresholds
    public const double MinSpeed = 0;      // Minimum acceptable speed (km/h)
    public const double MaxSpeed = 300;    // Maximum acceptable speed (km/h)

    // RPM thresholds
    public const double MinRpm = 500;      // Minimum acceptable RPM
    public const double MaxRpm = 12000;    // Maximum acceptable RPM

    // Acceleration/Braking thresholds
    public const double HarshAccelerationThreshold = 3.0;  // m/s²
    public const double HarshBrakingThreshold = -4.0;      // m/s²

    // High-Speed & Engine Load thresholds
    public const double HighSpeedThreshold = 120.0;        // km/h
    public const int HighRpmThreshold = 4000;             // RPM
    public const double HighEngineLoadThreshold = 75.0;   // %

    // Engine Load Value Filtering
    public const double MinEngineLoad = 500;     // Minimum valid engine load value
    public const double MaxEngineLoad = 12000;   // Maximum valid engine load value

    // Penalty values for eco-score
    public const int HarshAccelerationPenalty = 2; 
    public const int HarshBrakingPenalty = 3; 
    public const int HighSpeedPenalty = 5; 
    public const int HighRpmPenalty = 2; 
    public const int HighEngineLoadPenalty = 10; 
}