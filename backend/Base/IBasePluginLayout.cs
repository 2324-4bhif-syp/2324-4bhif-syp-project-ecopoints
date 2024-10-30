namespace Base;

public interface IBasePluginLayout
{
    Task<IEnumerable<Dictionary<string, object>>> ExecuteQuery(QueryParameters parameters);
}