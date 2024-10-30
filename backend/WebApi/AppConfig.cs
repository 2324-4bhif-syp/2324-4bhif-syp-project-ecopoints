namespace WebApi;

public class AppConfig
{
    public Plugins AllPlugins { get; set; }
    
    public class Plugins
    {
        public string PluginsFolderPath { get; set; }
    }
}