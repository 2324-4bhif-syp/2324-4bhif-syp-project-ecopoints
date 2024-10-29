using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Abstractions.Model
{
    public class Trip
    {
        public Guid TripId { get; set; }
        public List<CarSensorData> Data { get; set; }
    }
}
