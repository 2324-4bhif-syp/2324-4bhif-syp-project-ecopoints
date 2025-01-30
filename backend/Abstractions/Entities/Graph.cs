using Abstractions.Entities;

namespace Abstractions.Entities;

public class Graph : BaseEntity
{
    public string Title { get; set; }
    public string IFrameLink { get; set; }
    
    public bool RequiresCalc { get; set; }
}