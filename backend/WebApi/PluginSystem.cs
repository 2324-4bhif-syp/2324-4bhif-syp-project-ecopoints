using System.Reflection;
using Attributes;
using Base;

namespace WebApi;

 public class PluginSystem
    {
        public string BasePath { get; }
        public IEnumerable<string> PluginPaths { get; }
        
        private bool initialized = false;
        private List<(string name, Type parameterType)> _plugins { get; } = new();

        public PluginSystem(string basePath)
        { 
            this.BasePath = basePath;
            this.PluginPaths = Directory.EnumerateFiles(this.BasePath);
        }
        public void Initialize()
        {
            if (!initialized)
            {
                lock (typeof(PluginSystem))
                {
                    if (!initialized)
                    {
                        try
                        {
                            Console.WriteLine($"Starting initialization. Plugin paths found: {PluginPaths.Count()}");

                            foreach (var pluginPath in PluginPaths)
                            {
                                try
                                {
                                    Console.WriteLine($"Loading plugin from path: {pluginPath}");
                                    var assembly = Assembly.LoadFrom(pluginPath);
                                    Console.WriteLine($"Assembly loaded: {assembly.FullName}");

                                    var pluginTypesWhere = assembly.GetTypes()
                                        .Where(t => typeof(IBasePluginLayout).IsAssignableFrom(t));
                                    
                                    var pluginTypesSelect = pluginTypesWhere
                                        .Select(t => new
                                        {
                                            Type = t,
                                            Attribute = t.GetCustomAttribute<PluginAttribute>()
                                        });
                                    
                                     var pluginTypes = pluginTypesSelect
                                        .Where(t => t.Attribute != null && !string.IsNullOrWhiteSpace(t.Attribute.Name) && t.Attribute.ParameterType != null);

                                    foreach (var pluginType in pluginTypes)
                                    {
                                        Console.WriteLine($"Found plugin: {pluginType.Type.FullName}, Name: {pluginType.Attribute.Name}, ParameterType: {pluginType.Attribute.ParameterType}");
                                        _plugins.Add((pluginType.Attribute.Name, pluginType.Type));
                                    }
                                }
                                catch (Exception ex)
                                {
                                    Console.WriteLine($"Failed to load or process assembly {pluginPath}: {ex.Message}");
                                }
                            }
                        }
                        finally
                        {
                            initialized = true;
                            Console.WriteLine($"Initialization completed. Total plugins loaded: {_plugins.Count}");
                        }
                    }
                }
            }
        }


        public Type FindPlugin(string name)
        {
            foreach (var plugin in _plugins)
            {
                var pluginType = plugin.parameterType;
                
                Console.WriteLine($"Checking plugin type: {pluginType.FullName}");
                var attribute = pluginType.GetCustomAttribute<PluginAttribute>();
                
                if (attribute != null && attribute.Name.Equals(name, StringComparison.OrdinalIgnoreCase))
                {
                    Console.WriteLine($"Found plugin: {pluginType.FullName}");
                    return pluginType;
                }
            }

            Console.WriteLine("No matching plugin found.");
            return null;
        }
        
        public IEnumerable<(string Name, Type ParameterType)> GetAllPlugins()
        {
            return _plugins;
        }

        public IEnumerable<string> GetPluginNames()
        {
            return _plugins.Select(p => p.name);
        }
    }