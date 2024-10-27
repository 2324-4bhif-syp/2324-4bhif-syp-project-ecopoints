using System.ComponentModel.DataAnnotations.Schema;

namespace Persistence.Entities;

[Table("ECO_BRAND")]
public class Brand : BaseEntity
{
    public string Name { get; set; }
}