import {Component} from '@angular/core';
import {FriendshipDto} from "../dtos/friendship.dto";
import {FriendService} from "../services/friend.service";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent {
  searchResults: FriendshipDto[] = [];
  friendRequests: FriendshipDto[] = [];

  searchErrorMessage: string | null = null;
  friendRequestErrorMessage: string | null = null;

  searchInput: string | null = null;

  constructor(private friendService: FriendService, private authService: AuthService) {
  }

  ngOnInit() {
    this.getFriendRequests();
  }

  findUser() {
    if (!this.searchInput) {
      this.searchErrorMessage = "Please a username something to search";
      this.searchResults = [];
      return;
    }
    this.searchErrorMessage = null;
    this.friendService.getUserByUsername(this.searchInput).subscribe(data => {
        this.searchResults = data;
        this.getFriendRequests();
        if (this.searchResults.length == 0) {
          this.searchErrorMessage = "No users found with username:   '" + this.searchInput + "'";
        } else {
          this.searchErrorMessage = null;
        }
      },
      error => {
        console.error("Error in searching: " + error);
        this.searchErrorMessage = error.message;
      })
  }

  getFriendRequests() {
    this.friendRequestErrorMessage = null;
    this.friendService.getFriendRequests().subscribe(
      data => {
        this.friendRequests = data;
        if (this.friendRequests.length == 0) {
          this.friendRequestErrorMessage = "No incoming friend requests!";
        } else {
          this.friendRequestErrorMessage = null;
        }
      }, error => {
        this.friendRequests = [];
        this.friendRequestErrorMessage = error.message;
      }
    );
  }
}
