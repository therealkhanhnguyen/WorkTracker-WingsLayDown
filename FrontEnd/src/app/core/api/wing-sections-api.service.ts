import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface WingSectionResponse {
  id: string;           // like "WS_0036YL_R"
  name: string;         // like "0036YL/R"
  description: string;
  svgRegionId: string;  // must matches SVG element id
}

@Injectable({
  providedIn: 'root'
})
export class WingSectionsApiService {

  constructor(private http: HttpClient) { }

  list(): Observable<WingSectionResponse[]>{
    return this.http.get<WingSectionResponse[]>('/api/wing-sections')
  }
}
