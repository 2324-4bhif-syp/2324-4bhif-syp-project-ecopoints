﻿using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace Persistence.Migrations
{
    /// <inheritdoc />
    public partial class initial_deployment : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Graphs",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Title = table.Column<string>(type: "text", nullable: false),
                    IFrameLink = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Graphs", x => x.Id);
                });

            migrationBuilder.InsertData(
                table: "Graphs",
                columns: new[] { "Id", "IFrameLink", "Title" },
                values: new object[,]
                {
                    { 1, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "Obd Speed" },
                    { 2, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=4&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "Gps Speed" },
                    { 3, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=5&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "Engine Rpm" },
                    { 4, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=7&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "Engine Load" },
                    { 5, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=6&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "Coolant Temp" },
                    { 6, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=2&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "longitude" },
                    { 7, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=3&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "latitude" },
                    { 8, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30d&var-ids=$ids&panelId=8&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "altitude" }
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Graphs");
        }
    }
}