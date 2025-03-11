using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace Persistence.Migrations
{
    /// <inheritdoc />
    public partial class calcGraphs : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.InsertData(
                table: "Graphs",
                columns: new[] { "Id", "IFrameLink", "RequiresCalc", "Title" },
                values: new object[,]
                {
                    { 9, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30&var-ids=$ids&panelId=9&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", true, "GpsVsObd" },
                    { 10, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30&var-ids=$ids&panelId=11&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", true, "Engine Load/RPM" }
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "Graphs",
                keyColumn: "Id",
                keyValue: 9);

            migrationBuilder.DeleteData(
                table: "Graphs",
                keyColumn: "Id",
                keyValue: 10);
        }
    }
}
