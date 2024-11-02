import { Component, Input } from '@angular/core';
import { Graph } from '../model/Graph';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrl: './graph.component.css'
})
export class GraphComponent {
  @Input() public graph: Graph | null = null;
  @Input() public visible: boolean = false;
  
  public constructor(
    public sanitizer: DomSanitizer, 
    public http: HttpClient) {
  }

  public getSafeUrl(url: string){
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
