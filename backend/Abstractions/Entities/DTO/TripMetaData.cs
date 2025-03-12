namespace Abstractions.Entities.DTO;

public class TripMetaData
{
    public DateTime StartDate { get; set; }
    public DateTime EndDate { get; set; }
    public TimeSpan Duration { get; set; }
    public Guid TripId { get; set; }
    public double Distance { get; set; }
    public double AverageSpeedGps { get; set; }
    public double AverageSpeedObd { get; set; }
    public double MaxSpeedGps { get; set; }
    public double MaxSpeedObd { get; set; }
    public double AverageRpm { get; set; }
    public double AverageEngineLoad { get; set; }
    public EcoPointsMetaData EcoPointsMetaData { get; set; }
    public List<Tuple<double, double>> LatLngList { get; set; }
}