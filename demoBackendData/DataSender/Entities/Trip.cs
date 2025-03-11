namespace DataSender.Entities;

public class Trip
{
    public Guid TripId { get; set; }
    public List<CarSensorData> Data { get; set; } = new List<CarSensorData>();

}