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
  empty: string = ' '
  userId: number = 0;
  hasMoreEvents: boolean = true;
  currentPage = 0;
  pageSize = 6;

  constructor(private userService: UserService, private route: ActivatedRoute, 
    private eventService: EventService, private authService: AuthService, 
    private friendService: FriendService) {
  }
  
  ngOnInit(): void {
    this.authService.currentUser.subscribe((usr: UserDTO | null) => {
      this.curUser = usr;
    });
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.curUser?.id == this.userId)
      this.isCurrentUserPage = true;
    if (this.userId) {
      this.userService.getUserById(this.userId).subscribe(
        (data: UserDTO) => {
          this.user = data;
        },
        (error) => {
          console.error('Error fetching event:', error);
        }
      );
      this.loadFriends()
      this.loadEvents()
    } else {
      console.error('Invalid user ID');
    }
  }

  loadFriends(): void {
    this.friendService.getFriends(this.userId).subscribe(
      data => {
        this.friends = data;
      })
  }

  loadEvents(){
    this.eventService.getUserEvents(this.userId, this.currentPage, this.pageSize).subscribe((data: EventDTO[]) => {
      if (data.length > 0) {
        this.events.push(...data); 
        this.currentPage++;
      } else {
        this.hasMoreEvents = false;  
      }    });
  }
  
  onLoadMore(): void {
    this.loadEvents();
  }
}
