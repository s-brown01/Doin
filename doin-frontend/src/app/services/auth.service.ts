import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private token: string | null = null;

  constructor(private apiService: ApiService){

  }

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.apiService.post('login', credentials).pipe(
      map((response: any) => {
        const token = response.token;
        this.setToken(token);
      }),
      catchError(this.handleError)
    );
  }

  register(registerData: { username: string; password: string }): Observable<any> {
    return this.apiService.post('register', registerData).pipe(
      map((response: any) => {
        return response;
      }),
      catchError(this.handleError)
    );
  }


  setToken(token: string): void {
    this.token = token;
    localStorage.setItem('authToken', token); 
  }

  getToken(): string | null {
    return this.token || localStorage.getItem('authToken');
  }

  clearToken(): void {
    this.token = null;
    localStorage.removeItem('authToken');
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      switch (error.status) {
        case 401:
          errorMessage = 'Invalid username or password!';
          break;
        case 500:
          errorMessage = 'Internal server error. Please try again later.';
          break;
        default:
          errorMessage = `Error: ${error.message}`;
      }
    }
    return throwError(errorMessage);
  }
}