namespace Base;

public class QueryParameters
{
    private DateTime? _startDate;
    private DateTime? _endDate;
    private IEnumerable<Guid>? _ids;

    public DateTime? StartDate 
    { 
        get => _startDate ?? DateTime.Now.AddMonths(-1).ToUniversalTime(); 
        set => _startDate = value?.ToUniversalTime(); 
    }
        
    public DateTime? EndDate 
    { 
        get => _endDate ?? DateTime.Now.ToUniversalTime(); 
        set => _endDate = value?.ToUniversalTime(); 
    }

    public IEnumerable<Guid>? Ids 
    { 
        get => _ids ?? new List<Guid>(); 
        set => _ids = value; 
    }
}