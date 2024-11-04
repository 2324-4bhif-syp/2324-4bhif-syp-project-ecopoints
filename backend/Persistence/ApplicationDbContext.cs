using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System.Diagnostics;
using Abstractions.Entities;
using Npgsql;

namespace Persistence;

public class ApplicationDbContext : DbContext
{

    public DbSet<Graph> Graphs { get; set; }
    
    public ApplicationDbContext()
    {
    }

    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
        : base(options)
    {
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        if (!optionsBuilder.IsConfigured)
        {
            var configuration = new ConfigurationBuilder()
                .SetBasePath(AppContext.BaseDirectory)
                .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                .Build();
            optionsBuilder
                .LogTo(msg => Debug.WriteLine(msg), Microsoft.Extensions.Logging.LogLevel.Debug, Microsoft.EntityFrameworkCore.Diagnostics.DbContextLoggerOptions.SingleLine | Microsoft.EntityFrameworkCore.Diagnostics.DbContextLoggerOptions.UtcTime)
                .UseNpgsql(configuration["ConnectionStrings:Default"]);
        }
    }


    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        
        SeedData(modelBuilder);
    }

    private void SeedData(ModelBuilder modelBuilder)
    {
        
        
        modelBuilder.Entity<Graph>().HasData(
           
            new Graph { Id = 1, Title = "Obd Speed", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 2, Title = "Gps Speed", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=4&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 3, Title = "Engine Rpm", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=5&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 4, Title = "Engine Load", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=7&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 5, Title = "Coolant Temp", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=6&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 6, Title = "longitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=2&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 7, Title = "latitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=3&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" },
            new Graph { Id = 8, Title = "altitude", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=8&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" }
        );
        
    }
}