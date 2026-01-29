import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from './api.config';


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
  private readonly baseUrl = '/wing-sections';

  constructor(private http: HttpClient) { }

  getAll(): Observable<WingSectionResponse[]>{
    return this.http.get<WingSectionResponse[]>(`${API_BASE_URL}/wing-sections`);
  }
}
