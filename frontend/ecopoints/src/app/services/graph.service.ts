import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Graph } from '../model/Graph';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GraphService {

  private apiUrl = 'http://localhost:5221/api/graphs'; 

  constructor(private http: HttpClient) { }

  getGraphs(): Observable<Graph[]> {
    return this.http.get<Graph[]>(this.apiUrl);
  }

}
