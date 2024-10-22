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
  searchFriends: FriendshipDto[] = [];
  mayKnowList: FriendshipDto[] = [];

  fofErrorMessage: string | null = null;
  searchErrorMessage: string | null = null;

  searchInput: string | null = null;

  constructor(private friendService: FriendService) {
  }

  ngOnInit(): void {
    this.loadFriends();
  }

  getFriends(): void {
    this.friendService.getFriendsOfFriends().subscribe(data => {
        this.mayKnowList = data;
        this.fofErrorMessage = null;
      },
      error => {
        console.error("Error in subscribing to Friends of Friends: " + error);
        this.fofErrorMessage = error.message;
      })
  }

  findFriend(){
    if (!this.searchInput){
      this.searchErrorMessage = "Please a username something to search";
      this.searchFriends = [];
      return;
    }
    this.searchErrorMessage = null;
    this.searchFriends = [
      new FriendshipDto("friend1", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend1", "test")),
      new FriendshipDto("friend2", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend2", "test")),
      new FriendshipDto("friend3", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend3", "test"))
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

  loadFriends(): void {
    this.mayKnowList = [
      new FriendshipDto("fof 1", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend1", "test")),
      new FriendshipDto("fof 2", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend2", "test")),
      new FriendshipDto("fof 3", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend3", "test")),
      new FriendshipDto("fof 4", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend4", "test")),
      new FriendshipDto("fof 5", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend5", "test")),
      new FriendshipDto("fof 6", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend6", "test"))
    ]
  }


}
