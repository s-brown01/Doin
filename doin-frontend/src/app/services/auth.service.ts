import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpHeaders } from '@angular/common/http';
import {catchError, map, Observable, of} from "rxjs";

/**
 * AuthService handles authentication-related operations such as
 * token validation by communicating with the backend API.
 *
 * Framework of this authorization service was found from
 * <a href="https://www.digitalocean.com/community/tutorials/angular-route-guards#using-the-canactivate-route-guard">here</a>.
 * It was refined to match specific project needs with help from ChatGPT.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private apiService: ApiService) {}

  /**
   * Validates a JWT token by sending it to the backend API.
   *
   * @param token The JWT token to validate
   * @returns An observable that emits the server's response
   */
  validateToken(token: string): Observable<{ valid: boolean; message?: string }> {
    console.error("Validating token");
    const tokenDTO = { token: token };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    console.log("Sending token to backend");
    return this.apiService.post('validateToken', tokenDTO, headers).pipe(
      map((response: { success: boolean; message: string }) => {
        // Check the success status and return the corresponding object
        return {
          valid: response.success,
          message: response.message
        };
      }),
      catchError((error) => {
        return of({ valid: false, message: "Error occurred while validating the token" });
      })
    );
  }


}
