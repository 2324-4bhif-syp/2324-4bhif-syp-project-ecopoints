import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Graph } from '../model/Graph';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GraphService { 

  private apiUrl = 'http://localhost:5221/api'
  constructor(private http: HttpClient) { }

  getGraphs(): Observable<Graph[]> {
    return this.http.get<Graph[]>(`${this.apiUrl}/graphs`);
  }


  getTripIds(): Observable<string[]> {
    return this.http.get<any[]>(`${this.apiUrl}/trips`).pipe(
      map(data => data.map(trip => trip.tripId)) // Extrahiere nur tripId als Array von Strings
    );
  }
  

  getTripData(tripId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/trip/${tripId}`);
  }
}
