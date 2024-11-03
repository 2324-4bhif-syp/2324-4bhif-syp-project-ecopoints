import { Component, OnInit } from '@angular/core';
import { Graph } from '../model/Graph';
import { GraphService } from '../services/graph.service';

@Component({
  selector: 'app-graph-overview',
  templateUrl: './graph-overview.component.html',
  styleUrls: ['./graph-overview.component.css']
})
export class GraphOverviewComponent implements OnInit {

  constructor(private graphService: GraphService) {}

  public graphs: Graph[] = [];
  public tripIds: string[] = [];
  public selectedTripId: string | null = null;
  public currentGraph: Graph | null = null;

  ngOnInit(): void {
    this.loadTripIds();
    this.loadGraphs();
  }

  loadTripIds(): void {
    this.graphService.getTripIds().subscribe(
      (data: string[]) => {
        this.tripIds = data;
      }
    );
  }

  loadGraphs(): void {
    this.graphService.getGraphs().subscribe(
      (data: Graph[]) => {
        this.graphs = data;
        if (this.graphs.length > 0) {
          this.currentGraph = this.graphs[0];
        }
      }
    );
  }

  onTripIdSelect(event: Event): void {
    const selectedTripId = (event.target as HTMLSelectElement).value;
    this.selectedTripId = selectedTripId;
    this.updateGraphLinks();
  }


  updateGraphLinks(): void {
    if (this.selectedTripId) {
      const tripIdPattern = /var-ids=([^&]*)/; 
      
      this.graphs = this.graphs.map(graph => ({
        ...graph,
        iFrameLink: graph.iFrameLink.replace(tripIdPattern, `var-ids=${this.selectedTripId}`)
      }));
      
  
      if (this.currentGraph) {
        const index = this.graphs.findIndex(g => g.id === this.currentGraph!.id);
        if (index !== -1) {
          this.currentGraph = this.graphs[index];
        }
      }
    }
  }
  

  selectGraph(index: number): void {
    this.currentGraph = this.graphs[index];
  }
}
