import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';
import { UserDTO } from '../dtos/user.dto';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient, private apiService : ApiService) { }

  getUserById(id: number): Observable<UserDTO> {
    let params: any = {};
    params.id = id;
    return this.http.get<UserDTO>(`${this.baseUrl}/users`, {params});  }

    getUserByName(username: string): Observable<UserDTO> {
        let params: any = {};
        params.username = username;
      
        return this.http.get<UserDTO>(`${this.baseUrl}/users`, { params }).pipe(
          tap((response) => console.log('Response:', response)),
          catchError((error) => {
            return throwError(error);
          })
        );
    }
}