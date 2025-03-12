namespace Abstractions.Entities.DTO;

public class TripMetaData
{
    public DateTime StartDate { get; set; }
    public DateTime EndDate { get; set; }
    public TimeSpan Duration { get; set; }
    public Guid TripId { get; set; }
    public Double Distance { get; set; }
    public Double AverageSpeedGps { get; set; }
    public Double AverageSpeedObd { get; set; }
    public Double MaxSpeedGps { get; set; }
    public Double MaxSpeedObd { get; set; }
    public Double AverageRpm { get; set; }
    public List<Tuple<Double,Double>> LatLngList { get; set; }
}