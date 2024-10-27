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

  searchErrorMessage: string | null = null;

  searchInput: string | null = null;

  constructor(private friendService: FriendService) {
  }

  findFriend(){
    if (!this.searchInput){
      this.searchErrorMessage = "Please a username something to search";
      this.searchResults = [];
      return;
    }
    this.searchErrorMessage = null;
    this.searchResults = [
      new FriendshipDto("friend1", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend1", "test"), 1),
      new FriendshipDto("friend2", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend2", "test"), 1),
      new FriendshipDto("friend3", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend3", "test"), 1)
    ]
    return;
    // this.friendService.getFriendByUsername(this.searchInput).subscribe(data => {
    //   this.searchFriends = data;
    //   this.searchErrorMessage = null
    // },
    //   error => {
    //   console.error("Error in searching: " + error);
    //   this.searchErrorMessage = error.message;
    // })
  }
}
