import {Injectable} from '@angular/core';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {ApiService} from './api.service';
import {UserDTO} from '../dtos/user.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'users';

  constructor(private apiService: ApiService) {
  }

  getUserById(id: number): Observable<UserDTO> {
    const params = {id: id.toString()}; // Ensure id is passed as a string
    return this.apiService.get(`${this.baseUrl}`, undefined, params); // Pass params as the 3rd argument
  }

  getUserByName(username: string): Observable<UserDTO> {
    const params = {username: username};
    return this.apiService.get(`${this.baseUrl}`, undefined, params).pipe(
      tap((response) => console.log('Response:', response)),
      catchError((error) => {
        console.error('Error fetching user:', error);
        return throwError(() => error);
      })
    );
  }

  uploadProfileImage(formData: FormData): Observable<any> {
    return this.apiService.put(`${this.baseUrl}/update-profile-img`, formData, {
      reportProgress: true,
      observe: 'events'
    });
  }
}
