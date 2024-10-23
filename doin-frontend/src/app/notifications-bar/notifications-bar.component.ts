import { Component } from '@angular/core';
import { ImageComponent } from '../image/image.component';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UserDTO } from '../dtos/user.dto';
import { Observable } from 'rxjs';
import {FriendshipDto, FriendshipStatus} from "../dtos/friendship.dto";
import {ImageDTO} from "../dtos/image.dto";

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrl: './notifications-bar.component.css'
})
export class NotificationsBarComponent {
  user: UserDTO | null = null;

  friendsList = [
    new FriendshipDto("username 1", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend1", "test"), 1),
    new FriendshipDto("username 2", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend2", "test"), 1),
    new FriendshipDto("username 3", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend3", "test"), 1),
    new FriendshipDto("username 4", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend4", "test"), 1),
    new FriendshipDto("username 5", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend5", "test"), 1),
    new FriendshipDto("username 6", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend6", "test"), 1)
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.currentUser.subscribe((user: UserDTO | null) => {
      this.user = user;
    });
  }

  logout(){
    this.authService.clearToken();
  }
}
