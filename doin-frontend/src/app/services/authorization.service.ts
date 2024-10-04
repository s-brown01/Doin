import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  constructor(private apiService: ApiService) {}

  validateToken(token: string) {
    const tokenDTO = { token: token };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    
    return this.apiService.post('validateToken', tokenDTO, headers);
  }

  
}
