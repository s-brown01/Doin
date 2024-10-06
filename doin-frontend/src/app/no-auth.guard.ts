import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class NoAuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const token = this.authService.getToken();

    if (token) {
      const isTokenExpired = this.authService.isTokenExpired(token);

      if (!isTokenExpired) {
        this.router.navigate(['/home']);
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }
}