import {HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, switchMap, throwError} from 'rxjs';
import {ApiService} from './api.service';
import {jwtDecode} from 'jwt-decode';
import {UserDTO} from '../dtos/user.dto';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public currentUser: Observable<UserDTO | null>;
  private token: string | null = null;
  private currentUserSubject: BehaviorSubject<UserDTO>;

  constructor(private apiService: ApiService, private userService: UserService) {
    const savedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<UserDTO>(savedUser ? JSON.parse(savedUser) : null);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.apiService.post('login', credentials).pipe(
      map((response: any) => {
        const token = response.token;
        this.setToken(token);

        return token;
      }),
      switchMap((token: string) => {
        return this.userService.getUserByName(credentials.username).pipe(
          map((user: any) => {
            localStorage.setItem('currentUser', JSON.stringify(user));
            this.currentUserSubject.next(user);
            console.log(user);
            return user;
          })
        );
      }),
      catchError(error => {
        // seeing if it is an unauthorized error
        if (error.status === 401) {
          console.log("Invalid username or password");
          return throwError(() => error);
        }
        console.error("ERROR WITH POSTING", error);
        return throwError(() => new Error('API request failed'));
      })
    );
  }

  register(registerData: {
    username: string,
    password: string,
    confirmPassword: string,
    securityQuestion: string,
    securityAnswer: string
  }): Observable<any> {
    return this.apiService.post('register', registerData).pipe(
      map((response: any) => {
        return response;
      }),
      catchError(this.handleError)
    );
  }

  forgotPassword(resetData: {
    securityQuestionAnswer: string;
    securityQuestionValue: string;
    username: string
  }): Observable<any> {
    return this.apiService.post('forgot-password', resetData).pipe(
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

  getCurrentUser(): UserDTO {
    return this.currentUserSubject.value;
  }

  isTokenExpired(token: string): boolean {
    const decoded: any = jwtDecode(token);
    const currentTime = Date.now() / 1000;

    return decoded.exp < currentTime;
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
          errorMessage = `Error: ${error.error}`;
      }
    }
    return throwError(errorMessage);
  }
}
