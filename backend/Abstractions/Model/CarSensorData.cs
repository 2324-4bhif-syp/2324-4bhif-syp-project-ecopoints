using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Abstractions.Model
{
    public class CarSensorData
    {
        [JsonProperty("timestamp")]
        public DateTime Timestamp { get; set; }
        
        [JsonProperty("CarData")]
        public CarData CarData { get; set; }
    }
}
