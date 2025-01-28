import { Component, OnInit } from '@angular/core';
import { Graph } from '../model/Graph';
import { GraphService } from '../services/graph.service';

@Component({
  selector: 'app-graph-overview',
  templateUrl: './graph-overview.component.html',
  styleUrls: ['./graph-overview.component.css']
})
export class GraphOverviewComponent implements OnInit {

  public graphs: Graph[] = [];
  public tripIds: string[] = [];
  public selectedTripId: string | null = null;
  public currentGraph: Graph | null = null;
  public showTripIdWarning: boolean = true; 

  public sidebarOpen = false;
  public selectedCategory: 'rohdaten' | 'berechnet' = 'rohdaten';
  public displayedGraphs: Graph[] = [];

  private rohdatenGraphs: Graph[] = [];
  private berechneteGraphs: Graph[] = [];

  constructor(private graphService: GraphService) {}


  ngOnInit(): void {
    this.loadTripIds();
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  loadTripIds(): void {
    this.graphService.getTripIds().subscribe(
      (data: string[]) => {
        this.tripIds = data;
      }
    );
  }
  

  onTripIdSelect(event: Event): void {
    const selectedTripId = (event.target as HTMLSelectElement).value;
    
    if (selectedTripId === 'null') {
      this.selectedTripId = null;
      this.showTripIdWarning = true;
      this.currentGraph = null;
      this.graphs = [];
    } else {
      this.selectedTripId = selectedTripId;
      this.showTripIdWarning = false;
      this.loadGraphsAndApplyTripId(); 
    }
  }

  loadGraphsAndApplyTripId(): void {
    this.graphService.getGraphs().subscribe(
        (data: Graph[]) => {
            // Graphen in Rohdaten- und Berechnete-Werte-Listen trennen
            this.rohdatenGraphs = data.filter(graph => !graph.requiresCalc);
            this.berechneteGraphs = data.filter(graph => graph.requiresCalc);

            this.updateGraphLinks();
            this.updateGraphList();
        }
    );
}

updateGraphLinks(): void {
  if (this.selectedTripId) {
      const tripIdPattern = /var-ids=\$ids/; // Suche genau nach $ids

      // Update Rohdaten-Graphen
      this.rohdatenGraphs = this.rohdatenGraphs.map(graph => ({
          ...graph,
          iFrameLink: graph.iFrameLink.replace(tripIdPattern, `var-ids=${this.selectedTripId}`)
      }));

      // Update Berechnete Graphen
      this.berechneteGraphs = this.berechneteGraphs.map(graph => ({
          ...graph,
          iFrameLink: graph.iFrameLink.replace(tripIdPattern, `var-ids=${this.selectedTripId}`)
      }));

      console.log("Graph-Links aktualisiert für Trip:", this.selectedTripId);
  }
}


  selectCategory(category: 'rohdaten' | 'berechnet'): void {
    this.selectedCategory = category;
    this.updateGraphList();
    this.toggleSidebar(); // Sidebar schließen nach Auswahl
    }

    updateGraphList(): void {
      this.displayedGraphs = this.selectedCategory === 'rohdaten' ? this.rohdatenGraphs : this.berechneteGraphs;
  }

    selectGraph(graph: Graph): void {
      this.currentGraph = graph;
      console.log("Ausgewählter Graph iFrame-Link:", graph.iFrameLink);
  }


}
