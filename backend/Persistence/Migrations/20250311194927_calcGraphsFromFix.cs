using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Persistence.Migrations
{
    /// <inheritdoc />
    public partial class calcGraphsFromFix : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Graphs",
                keyColumn: "Id",
                keyValue: 9,
                column: "IFrameLink",
                value: "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now-30d&to=now&var-ids=$ids&panelId=9&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}");

            migrationBuilder.UpdateData(
                table: "Graphs",
                keyColumn: "Id",
                keyValue: 10,
                column: "IFrameLink",
                value: "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now-30d&to=now&var-ids=$ids&panelId=11&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Graphs",
                keyColumn: "Id",
                keyValue: 9,
                column: "IFrameLink",
                value: "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30&var-ids=$ids&panelId=9&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}");

            migrationBuilder.UpdateData(
                table: "Graphs",
                keyColumn: "Id",
                keyValue: 10,
                column: "IFrameLink",
                value: "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=now&to=now-30&var-ids=$ids&panelId=11&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}");
        }
    }
}
