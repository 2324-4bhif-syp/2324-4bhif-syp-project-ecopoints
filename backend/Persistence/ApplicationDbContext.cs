using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System.Diagnostics;
using Abstractions.Entities;
using Npgsql;

namespace Persistence;

public class ApplicationDbContext : DbContext
{

    private readonly IConfiguration _configuration;

    public DbSet<Graph> Graphs { get; set; }

    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options, IConfiguration configuration)
        : base(options)
    {
        _configuration = configuration;
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        if (!optionsBuilder.IsConfigured)
        {
            optionsBuilder
                .LogTo(msg => Debug.WriteLine(msg), Microsoft.Extensions.Logging.LogLevel.Debug, 
                    Microsoft.EntityFrameworkCore.Diagnostics.DbContextLoggerOptions.SingleLine | 
                    Microsoft.EntityFrameworkCore.Diagnostics.DbContextLoggerOptions.UtcTime)
                .UseNpgsql(_configuration.GetConnectionString("Default"));
        }
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        SeedData(modelBuilder);
    }

    private void SeedData(ModelBuilder modelBuilder)
    {
        
        
        modelBuilder.Entity<Graph>().HasData(
           
            new Graph { Id = 1, Title = "Obd Speed", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false},
            new Graph { Id = 2, Title = "Gps Speed", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=4&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 3, Title = "Engine Rpm", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=5&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 4, Title = "Engine Load", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=7&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 5, Title = "Coolant Temp", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=6&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 6, Title = "longitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=2&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 7, Title = "latitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=3&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 8, Title = "altitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=8&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = false },
            new Graph { Id = 9, Title = "GpsVsObd", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now-30d&to=now&var-ids=$ids&panelId=9&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = true },
            new Graph { Id = 10, Title = "Engine Load/RPM", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now-30d&to=now&var-ids=$ids&panelId=11&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", RequiresCalc = true }

        );
        
    }
}