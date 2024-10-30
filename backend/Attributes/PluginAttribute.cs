namespace Attributes;
[AttributeUsage(AttributeTargets.Class, Inherited = false, AllowMultiple = false)]
public class PluginAttribute : Attribute
{
    public string Name { get; }
    public Type ParameterType { get; }

    public PluginAttribute(string name, Type parameterType)
    {
        Name = name;
        ParameterType = parameterType;
    }
}