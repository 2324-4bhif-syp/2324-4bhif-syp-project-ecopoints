import { Component, OnInit } from '@angular/core';
import { Graph } from '../model/Graph';

@Component({
  selector: 'app-graph-overview',
  templateUrl: './graph-overview.component.html',
  styleUrl: './graph-overview.component.css'
})
export class GraphOverviewComponent implements OnInit {
  public graphs: Graph[] = [
    { id: 1, title: 'Graph 1', iFrameLink: 'http://localhost:3000/d-solo/ee2r6d08shr7ka/eco-points?orgId=1&from=1730073573402&to=1730419173402&var-ids=5fa85f64-5717-4562-b3fc-2c963f66afa6&panelId=1&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}' },
    { id: 2, title: 'Graph 2', iFrameLink: 'https://grafana.com/your-graph2' },
    { id: 3, title: 'Graph 3', iFrameLink: 'https://grafana.com/your-graph3' },
  ];
  public currentGraph: Graph | null = null;

  ngOnInit(): void {
    this.currentGraph = this.graphs[0]; 
  }

  selectGraph(index: number): void {
    this.currentGraph = this.graphs[index];
  }
}