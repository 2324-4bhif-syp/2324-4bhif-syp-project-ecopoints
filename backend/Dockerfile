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
RUN dotnet publish -c Release -o out

# Runtime image
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /App
COPY --from=build-env /App/WebApi/out .
ENTRYPOINT ["dotnet", "WebApi.dll"]
