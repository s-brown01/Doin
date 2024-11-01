import { Component } from '@angular/core';
import { UserDTO } from '../dtos/user.dto';
import { UserService } from '../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { EventDTO } from '../dtos/event.dto';
import { EventService } from '../services/event.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css'
})
export class UserPageComponent {
  curUser: UserDTO | null = null;
  user: UserDTO | null = null; 
  events: EventDTO[] = [];
  buttonTitle: string = 'Chanage Profile Pic'
  isCurrentUserPage: boolean = false;

  constructor(private userService: UserService, private route: ActivatedRoute, private eventService: EventService, private authService: AuthService) {
  }
  
  ngOnInit(): void {
    this.authService.currentUser.subscribe((usr: UserDTO | null) => {
      this.curUser = usr;
    });

    this.eventService.getEvents(0, 10).subscribe((data: EventDTO[]) => {
      this.events = data;
    });
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.curUser?.id == userId)
      this.isCurrentUserPage = true;
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
