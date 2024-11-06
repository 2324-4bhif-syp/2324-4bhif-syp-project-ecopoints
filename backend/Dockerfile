# Build image
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build-env
WORKDIR /App

# Copy the solution file
COPY backend.sln ./

# Copy each referenced project
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

# Restore dependencies for the entire solution
RUN dotnet restore

# Copy the entire source code
COPY . ./

# Publish the WebApi project
WORKDIR /App/WebApi
RUN dotnet publish -c Release -o ../PluginsOutput

# Create the PluginsOutput directory in the Docker container
RUN mkdir -p /App/PluginsOutput

# Move the plugins to PluginsOutput within build-env
RUN cp -r /App/WebApi/out/*.dll /App/PluginsOutput/

# Runtime image
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /App

# Copy appsettings and other necessary files
COPY WebApi/appsettings.json /App/appsettings.json

# Copy the output, including plugins
COPY --from=build-env /App/PluginsOutput /App/PluginsOutput
COPY --from=build-env /App/WebApi/out .

ENTRYPOINT ["dotnet", "WebApi.dll"]
