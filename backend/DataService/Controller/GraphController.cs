using Abstractions.Entities;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using Persistence;

namespace DataService.Controller;

public class GraphController
{
    private readonly ApplicationDbContext _dbContext;

    public GraphController(ApplicationDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<IResult> GetAllGraphs()
    {
        var graphs = await _dbContext.Graphs.ToListAsync();
        return graphs.Count == 0 ? Results.NotFound("No graphs found") : Results.Ok(graphs);
    }

    public async Task<IResult> GetGraphById(int id)
    {
        var graph = await _dbContext.Graphs.FindAsync(id);
        return graph == null ? Results.NotFound("Graph not found") : Results.Ok(graph);
    }

    public async Task<IResult> AddGraph(Graph graph)
    {
        _dbContext.Graphs.Add(graph);
        await _dbContext.SaveChangesAsync();
        return Results.Ok("Graph added successfully");
    }

    public async Task<IResult> UpdateGraph(int id, Graph updatedGraph)
    {
        var graph = await _dbContext.Graphs.FindAsync(id);
        if (graph == null)
        {
            return Results.NotFound("Graph not found");
        }

        graph.Title = updatedGraph.Title;
        graph.IFrameLink = updatedGraph.IFrameLink;

        _dbContext.Graphs.Update(graph);
        await _dbContext.SaveChangesAsync();
        return Results.Ok("Graph updated successfully");
    }

    public async Task<IResult> DeleteGraph(int id)
    {
        var graph = await _dbContext.Graphs.FindAsync(id);
        if (graph == null)
        {
            return Results.NotFound("Graph not found");
        }

        _dbContext.Graphs.Remove(graph);
        await _dbContext.SaveChangesAsync();
        return Results.Ok("Graph deleted successfully");
    }
}