import { Component } from '@angular/core';
import { UserDTO } from '../dtos/user.dto';
import { UserService } from '../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { EventDTO } from '../dtos/event.dto';
import { EventService } from '../services/event.service';
import { AuthService } from '../services/auth.service';
import { FriendshipDto } from '../dtos/friendship.dto';
import { FriendService } from '../services/friend.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css'
})
export class UserPageComponent {
  curUser: UserDTO | null = null;
  user: UserDTO | null = null; 
  events: EventDTO[] = [];
  friends: FriendshipDto[] = [];
  buttonTitle: string = 'Chanage Profile Pic'
  isCurrentUserPage: boolean = false;

  constructor(private userService: UserService, private route: ActivatedRoute, 
    private eventService: EventService, private authService: AuthService, 
    private friendService: FriendService) {
  }
  
  ngOnInit(): void {
    this.authService.currentUser.subscribe((usr: UserDTO | null) => {
      this.curUser = usr;
    });
    this.loadFriends()
    this.laodEvents()
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

  loadFriends(): void {
    this.friendService.getFriends().subscribe(
      data => {
        this.friends = data;
      })
  }

  laodEvents(){
    this.eventService.getEvents(0, 10).subscribe((data: EventDTO[]) => {
      this.events = data;
    });
  }
}
