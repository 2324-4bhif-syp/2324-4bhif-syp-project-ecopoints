import { Component, OnInit } from '@angular/core';
import { Graph } from '../model/Graph';
import { GraphService } from '../services/graph.service';

@Component({
  selector: 'app-graph-overview',
  templateUrl: './graph-overview.component.html',
  styleUrl: './graph-overview.component.css'
})
export class GraphOverviewComponent implements OnInit {

  constructor(private graphService: GraphService) {}

  public graphs: Graph[] = [];
  public currentGraph: Graph | null = null;


  ngOnInit(): void {
    this.loadGraphs();
  }

  loadGraphs(): void {
    this.graphService.getGraphs().subscribe(
      (data: Graph[]) => {
        this.graphs = data;
        if (this.graphs.length > 0) {
          this.currentGraph = this.graphs[0];
        }
      },
      error => {
        console.error('Error loading graphs:', error);
      }
    );
  }

  selectGraph(index: number): void {
    this.currentGraph = this.graphs[index];
  }
}