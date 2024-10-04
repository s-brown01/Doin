import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpHeaders } from '@angular/common/http';

/**
 * Framework of this authorization service was found from <a href = https://www.digitalocean.com/community/tutorials/angular-route-guards#using-the-canactivate-route-guard>here</a>.
 * It was refined to match specific project with help from ChatGPT
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private apiService: ApiService) {}

  validateToken(token: string) {
    const tokenDTO = { token: token };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.apiService.post('validateToken', tokenDTO, headers);
  }


}
