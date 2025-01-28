import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Graph } from '../model/Graph';
import { GraphService } from '../services/graph.service';

@Component({
  selector: 'app-modify-graphs',
  templateUrl: './modify-graphs.component.html',
  styleUrl: './modify-graphs.component.css'
})
export class ModifyGraphsComponent implements OnInit {
  graph: Graph = {
    title: '',
    iFrameLink: '',
    requiresCalc: false
  };

  graphs: Graph[] = []; 
  iFrameLinkError: string | null = null;

  constructor(private http: HttpClient, private router: Router, private graphService: GraphService) {}

  ngOnInit(): void {
    this.loadGraphs();
  }


  loadGraphs(): void {
    this.graphService.getGraphs().subscribe(graphs => {
      this.graphs = graphs;
    })}

    onGraphSelect(graphId: string): void {
      if (graphId === 'null') {
        this.graph = {
          title: '',
          iFrameLink: '',
          requiresCalc: false
        };
      } else {
        const selectedGraph = this.graphs.find(g => g.id === Number(graphId));
        if (selectedGraph) {
          this.graph = { ...selectedGraph }; 
        }
        if (this.graph.requiresCalc === undefined || this.graph.requiresCalc === null) {
          this.graph.requiresCalc = false;
      }
      }
    }

    openDocumentation(): void {
      this.router.navigate(['/documentation']);
    }

    convertRequiresCalc(): void {
      this.graph.requiresCalc = !!this.graph.requiresCalc; // Konvertiert zu Boolean
      console.log("requiresCalc Wert nach Umwandlung:", this.graph.requiresCalc);
  }  
  
    
  addGraph(): void {
    console.log("Attempting to add graph...");

    if (!this.validateIFrameLink(this.graph.iFrameLink)) {
      this.iFrameLinkError = 'Invalid iFrame link format. Please ensure it contains panelId, orgId, and other required parameters.';
      return; 
    }


    this.iFrameLinkError = null;
    this.graph.iFrameLink = this.appendParametersToUrl(this.graph.iFrameLink);
    
    // Extract the panelId from the updated iFrameLink
    const panelId = this.extractPanelIdFromUrl(this.graph.iFrameLink);

    this.http.post<Graph>('http://localhost:5221/api/graph', this.graph)
        .subscribe({
          next: () => {
            console.log("Graph added successfully!");
            this.router.navigate(['/']);
          },
          error: error => {
            console.error('There was an error adding the graph:', error);
            alert('There was an error adding the graph. Please try again.');
          }
        });
  }

  updateGraph(): void {
    if (!this.validateIFrameLink(this.graph.iFrameLink)) {
      this.iFrameLinkError = 'Invalid iFrame link format. Please ensure it contains panelId, orgId, and other required parameters.';
      return; 
    }
  
    this.graph.iFrameLink = this.appendParametersToUrl(this.graph.iFrameLink);
    
    this.http.put<Graph>(`http://localhost:5221/api/graph/${this.graph.id}`, this.graph)
      .subscribe({
        next: () => {
          console.log("Graph updated successfully!");
          this.router.navigate(['/']);
        },
        error: error => {
          console.error('There was an error updating the graph:', error);
          alert('There was an error updating the graph. Please try again.');
        }
      });
  }
  
  deleteGraph(): void {
    if (!this.graph.id) {
      alert('No graph selected!');
      return;
    }
  
    if (confirm('Are you sure you want to delete this graph?')) {
      this.http.delete(`http://localhost:5221/api/graph/${this.graph.id}`).subscribe({
        next: () => {
          alert('Graph deleted successfully!');
          this.graphs = this.graphs.filter(g => g.id !== this.graph.id); 
          this.graph = { 
            title: '',
            iFrameLink: '',
            requiresCalc: false
          };
          this.loadGraphs(); 
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('There was an error deleting the graph:', error);
          alert('Failed to delete the graph. Please try again.');
        }
      });
    }
  }
  

  /**
   * Validates the iFrame link to ensure it follows the required structure.
   * @param url The URL to validate.
   * @returns True if the URL is valid, otherwise false.
   */
  validateIFrameLink(url: string): boolean {
    try {
      const urlObject = new URL(url);

      // Check for required parameters
      const hasPanelId = urlObject.searchParams.has('panelId');
      const hasOrgId = urlObject.searchParams.has('orgId');
      const hasVarId = urlObject.searchParams.has('var-ids');

      // Return true only if all required parameters are present
      return hasPanelId && hasOrgId && hasVarId;
    } catch (error) {
      console.error('URL validation failed:', error);
      return false;
    }
  }

  /**
   * Extracts the panelId from the given iFrame URL.
   * @param url The URL from which to extract the panelId.
   * @returns The extracted panelId as a number, or null if not found.
   */
  extractPanelIdFromUrl(url: string): number | null {
    try {
      const urlObject = new URL(url);
      const panelIdString = urlObject.searchParams.get('panelId');
      return panelIdString ? parseInt(panelIdString, 10) : null;
    } catch (error) {
      console.error('Failed to parse URL:', error);
      return null;
    }
  }

  /**
   * Appends specified query parameters and CSS to the given URL.
   * @param url The original URL provided by the user.
   * @returns The modified URL with additional parameters.
   */
  appendParametersToUrl(url: string): string {
    const additionalParams = '&theme=light&css=.panel-container,.graph-panel{background-color:white !important;}.flot-background{fill:white !important;}';
    // Check if URL already contains query parameters
    if (url.includes('?')) {
      return url + additionalParams;
    } else {
      return url + '?' + additionalParams.substring(1);
    }
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
