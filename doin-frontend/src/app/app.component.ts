import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { MenuBarComponent } from './menu-bar/menu-bar.component';
import { NotificationsBarComponent } from "./notifications-bar/notifications-bar.component";
import { StoryBarComponent } from './story-bar/story-bar.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, MenuBarComponent, NotificationsBarComponent, StoryBarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'doin-frontend';
  constructor(private router: Router) {}
  isBlankLayout(): boolean {
    return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/forgot-password';
  }
}
