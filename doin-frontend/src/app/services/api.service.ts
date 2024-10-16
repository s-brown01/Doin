import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getString(endpoint: string, headers?: HttpHeaders): Observable<string> {
    return (this.http.get(`${this.baseUrl}/${endpoint}`, { headers, responseType: 'string' as 'json' })) as Observable<string>;
  }

  post(endpoint: string, data: any, headers?: HttpHeaders): Observable<any> {
    return this.http.post(`${this.baseUrl}/${endpoint}`, data, { headers }).pipe(
      catchError(error => {
        if (error.status === 401) {
          return throwError(() => error);
        }
        console.error("Unexpected error - ", error);
        return throwError(() => new Error('API request failed'));
      })
    );
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
