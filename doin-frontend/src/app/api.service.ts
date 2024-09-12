import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  post(endpoint: string, data: any, headers?: HttpHeaders): Observable<any> {
    return this.http.post(`${this.baseUrl}/${endpoint}`, data, { headers });
  }

  get(endpoint: string, headers?: HttpHeaders): Observable<any> {
    return this.http.get(`${this.baseUrl}/${endpoint}`, { headers });
  }

  put(endpoint: string, data: any, headers?: HttpHeaders): Observable<any> {
    return this.http.put(`${this.baseUrl}/${endpoint}`, data, { headers });
  }

  delete(endpoint: string, headers?: HttpHeaders): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${endpoint}`, { headers });
  }
}