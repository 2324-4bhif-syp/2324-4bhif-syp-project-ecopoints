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
           
            new Graph { Id = 1, Title = "Engine RPM", IFrameLink = "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=1730073573402&to=1730419173402&var-ids=5fa85f64-5717-4562-b3fc-2c963f66afa6&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}" }
        );
        
    }
}