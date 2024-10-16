import { Component } from '@angular/core';
import { UserDTO } from '../dtos/user.dto';
import { UserService } from '../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { EventDTO } from '../dtos/event.dto';
import { EventService } from '../services/event.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css'
})
export class UserPageComponent {
  user: UserDTO | null = null; 
  events: EventDTO[] = [];

  constructor(private userService: UserService, private route: ActivatedRoute, private eventService: EventService) {
  }

  ngOnInit(): void {
    this.eventService.getEvents().subscribe((data: EventDTO[]) => {
      this.events = data;
    });
    const userId = Number(this.route.snapshot.paramMap.get('id'));
      if (userId) {
      this.userService.getUserById(userId).subscribe(
        (data: UserDTO) => {
          this.user = data;
        },
        (error) => {
          console.error('Error fetching event:', error);
        }
      );
    } else {
      console.error('Invalid user ID');
    }
  }
}
