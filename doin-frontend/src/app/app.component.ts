import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'doin-frontend';

  constructor(private router: Router) {}

  isBlankLayout(): boolean {
    return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/forgot-password';
  }
}