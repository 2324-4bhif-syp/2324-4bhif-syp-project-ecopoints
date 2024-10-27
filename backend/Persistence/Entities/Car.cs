using System.ComponentModel.DataAnnotations.Schema;

namespace Persistence.Entities;

[Table("ECO_CAR")]
public class Car : BaseEntity
{
    public string Model { get; set; }
    
    public Brand Brand { get; set; }
    public long BrandId { get; set; }
}