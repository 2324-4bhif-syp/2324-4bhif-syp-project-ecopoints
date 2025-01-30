import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Graph } from '../model/Graph';
import { GraphService } from '../services/graph.service';

@Component({
  selector: 'app-documentation',
  templateUrl: './documentation.component.html',
  styleUrl: './documentation.component.css'
})
export class DocumentationComponent {

  constructor(private http: HttpClient, private router: Router, private graphService: GraphService) {}


  goBack(): void {
    this.router.navigate(['/modify-graphs']);
  }

}
