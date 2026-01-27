import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from './api.config';

export interface JobResponse {
  id: number;
  wingSectionId: string;
  assignedEmployeeId: number | null;
  title: string;
  description: string;
  status: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface JobCreateRequest {
  wingSectionId: string;
  assignedEmployeeId?: number | null;
  title: string;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class JobsApiService {
  private readonly baseUrl = `${API_BASE_URL}/api/jobs`;

  constructor(private http: HttpClient) {}

  list(status?: string, wingSectionId?: string): Observable<JobResponse[]> {
    let params = new HttpParams();
    if (status) params = params.set('status', status);
    if (wingSectionId) params = params.set('wingSectionId', wingSectionId);

    return this.http.get<JobResponse[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<JobResponse> {
    return this.http.get<JobResponse>(`${this.baseUrl}/${id}`);
  }

  create(payload: JobCreateRequest): Observable<JobResponse> {
    return this.http.post<JobResponse>(this.baseUrl, payload);
  }

  updateStatus(id: number, status: string): Observable<JobResponse> {
    return this.http.patch<JobResponse>(`${this.baseUrl}/${id}/status`, { status });
  }

  requestFinal(id: number): Observable<JobResponse> {
    return this.http.patch<JobResponse>(`${this.baseUrl}/${id}/request-final`, {});
  }

}
