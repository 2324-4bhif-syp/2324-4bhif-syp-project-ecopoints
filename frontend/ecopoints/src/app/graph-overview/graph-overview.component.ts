import { Component, OnInit } from '@angular/core';
import { Graph } from '../model/Graph';
import { GraphService } from '../services/graph.service';
import { Duration } from '../model/Duration';

@Component({
  selector: 'app-graph-overview',
  templateUrl: './graph-overview.component.html',
  styleUrls: ['./graph-overview.component.css']
})
export class GraphOverviewComponent implements OnInit {

  public graphs: Graph[] = [];
  public tripList: { tripId: string, date: string }[] = [];  
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
    this.loadTrips();
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

  

  async loadTrips(): Promise<void> {
    try {
      const tripIds = await this.graphService.getTripIds().toPromise();
  
      if (!tripIds || tripIds.length === 0) {
        console.log("🚨 Keine Trip-IDs erhalten.");
        return;
      }
  
      console.log("📌 Empfangene Trip-IDs als Strings:", tripIds);
  
      const tripRequests = tripIds.map(async (tripId: string) => {
        console.log("🔎 tripId vor API-Aufruf:", tripId, "Type:", typeof tripId);
  
        const tripData = await this.graphService.getTripData(tripId).toPromise();
  
        if (!Array.isArray(tripData) || tripData.length === 0) {
          return { tripId, date: "Unknown Date" };
        }
  
        return {
          tripId,
          date: new Date(tripData[0].timestamp).toLocaleString()
        };
      });
  
      this.tripList = await Promise.all(tripRequests);
    } catch (error) {
      console.error("❌ Fehler beim Laden der Trips:", error);
    }
  }
  
  


  onTripSelect(event: Event): void {
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
    this.toggleSidebar();
    }

    updateGraphList(): void {
      this.displayedGraphs = this.selectedCategory === 'rohdaten' ? this.rohdatenGraphs : this.berechneteGraphs;
  }

    selectGraph(graph: Graph): void {
      this.currentGraph = graph;
      console.log("Ausgewählter Graph iFrame-Link:", graph.iFrameLink);
  }


}
