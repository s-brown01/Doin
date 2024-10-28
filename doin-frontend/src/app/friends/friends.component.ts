import {Component} from '@angular/core';
import {FriendshipDto, FriendshipStatus} from "../dtos/friendship.dto";
import {FriendService} from "../services/friend.service";
import {ImageDTO} from "../dtos/image.dto";
import {error} from "@angular/compiler-cli/src/transformers/util";

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

  constructor(private friendService: FriendService) {
  }

  ngOnInit(){
    this.getFriendRequests();
  }

  findUser(){
    if (!this.searchInput){
      this.searchErrorMessage = "Please a username something to search";
      this.searchResults = [];
      return;
    }
    this.searchErrorMessage = null;
    this.friendService.getUserByUsername(this.searchInput).subscribe(data => {
      this.searchResults = data;

      if (this.searchResults.length == 0){
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
        this.friendRequestErrorMessage = "WORKING SO FAR";
      }, error => {
        this.friendRequests = [];
        this.friendRequestErrorMessage = error.message;
      }
    );
    // this.friendRequests = [
    //   new FriendshipDto("request1", FriendshipStatus.PENDING, new ImageDTO(1, "friend1", "test"), 1),
    //   new FriendshipDto("request2", FriendshipStatus.PENDING, new ImageDTO(1, "friend2", "test"), 1),
    //   new FriendshipDto("request3", FriendshipStatus.PENDING, new ImageDTO(1, "friend3", "test"), 1)
    // ]
  }
}
