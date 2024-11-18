import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'doin-frontend';
  showNotifications: boolean = false;
  isMobile: boolean = false;

  constructor(private router: Router) {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth <= 1024;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.checkScreenSize();
  }

  isBlankLayout(): boolean {
    return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/forgot-password';
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }
}