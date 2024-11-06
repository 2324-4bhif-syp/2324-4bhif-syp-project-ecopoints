# Build image
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build-env
WORKDIR /App

# Copy the solution file and project files for all plugins and dependencies
COPY backend.sln ./
COPY Abstractions/Abstractions.csproj ./Abstractions/
COPY Attributes/Attributes.csproj ./Attributes/
COPY Base/Base.csproj ./Base/
COPY DataService/DataService.csproj ./DataService/
COPY Persistence/Persistence.csproj ./Persistence/
COPY WebApi/WebApi.csproj ./WebApi/
COPY ObdSpeedPlugin/ObdSpeedPlugin.csproj ./ObdSpeedPlugin/
COPY LatitudePlugin/LatitudePlugin.csproj ./LatitudePlugin/
COPY LongitudePlugin/LongitudePlugin.csproj ./LongitudePlugin/
COPY AltitudePlugin/AltitudePlugin.csproj ./AltitudePlugin/
COPY EngineLoadPlugin/EngineLoadPlugin.csproj ./EngineLoadPlugin/
COPY CoolantTemperaturePlugin/CoolantTemperaturePlugin.csproj ./CoolantTemperaturePlugin/
COPY EngineRpmPlugin/EngineRpmPlugin.csproj ./EngineRpmPlugin/
COPY GpsSpeedPlugin/GpsSpeedPlugin.csproj ./GpsSpeedPlugin/

# Restore dependencies
RUN dotnet restore

# Copy the entire source code
COPY . ./

# Create PluginsOutput directory and publish each plugin into it
RUN mkdir -p /App/PluginsOutput && \
    dotnet publish ObdSpeedPlugin/ObdSpeedPlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish LatitudePlugin/LatitudePlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish LongitudePlugin/LongitudePlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish AltitudePlugin/AltitudePlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish EngineLoadPlugin/EngineLoadPlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish CoolantTemperaturePlugin/CoolantTemperaturePlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish EngineRpmPlugin/EngineRpmPlugin.csproj -c Release -o /App/PluginsOutput && \
    dotnet publish GpsSpeedPlugin/GpsSpeedPlugin.csproj -c Release -o /App/PluginsOutput

# Publish the WebApi project
RUN dotnet publish WebApi/WebApi.csproj -c Release -o /App/out

# Runtime image
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /App

# Copy the output, including plugins
COPY --from=build-env /App/out .
COPY --from=build-env /App/PluginsOutput /App/PluginsOutput
COPY WebApi/appsettings.json /App/


# Start the Web API
ENTRYPOINT ["dotnet", "WebApi.dll"]
