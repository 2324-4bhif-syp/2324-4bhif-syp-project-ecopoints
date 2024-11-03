using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace Persistence.Migrations
{
    /// <inheritdoc />
    public partial class initial : Migration
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
                values: new object[] { 1, "http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=1730073573402&to=1730419173402&var-ids=5fa85f64-5717-4562-b3fc-2c963f66afa6&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}", "Engine RPM" });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Graphs");
        }
    }
}
