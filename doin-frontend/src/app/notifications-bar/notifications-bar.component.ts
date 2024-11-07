import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UserDTO } from '../dtos/user.dto';
import { EventDTO, EventType } from '../dtos/event.dto';
import { EventService } from '../services/event.service';

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrl: './notifications-bar.component.css'
})
export class NotificationsBarComponent {
  user: UserDTO | null = null;
  upcomingEvents: EventDTO[] = [];

  constructor(private authService: AuthService, private router: Router, private eventService: EventService) {}
  ngOnInit(): void {
    this.authService.currentUser.subscribe((user: UserDTO | null) => {
      this.user = user;
    });
    this.eventService.getUpcomingEvents().subscribe({
      next: (newEvents: EventDTO[]) => {
          this.upcomingEvents.push(...newEvents);
      },
      error: (error) => {
        console.error('Error loading upcoming events:', error);
      }
    });
  }
  getShortDescription(description: string): string {
    return description.length > 18 ? description.slice(0, 15) + '...' : description;
  }
  goToProfile(): void {
    if (this.user) {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/user', this.user?.id]);
      });
    }
  }

  logout(){
    this.authService.clearToken();
  }
}
