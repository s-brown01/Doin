import {Component} from '@angular/core';
import {FriendshipDto, FriendshipStatus} from "../dtos/friendship.dto";
import {FriendService} from "../services/friend.service";
import {ImageDTO} from "../dtos/image.dto";

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent {
  friendsList: FriendshipDto[] = [];
  fofList: FriendshipDto[] = [];

  constructor(private friendService: FriendService) {
  }

  ngOnInit(): void {
    this.getFriends();
  }

  getFriends(): void {
    this.friendsList = [
      new FriendshipDto("friend1", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend1", "test")),
      new FriendshipDto("friend2", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend2", "test")),
      new FriendshipDto("friend3", FriendshipStatus.CONFIRMED, new ImageDTO(1, "friend3", "test"))
    ]
    this.fofList = [
      new FriendshipDto("fof 1", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend1", "test")),
      new FriendshipDto("fof 2", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend2", "test")),
      new FriendshipDto("fof 3", FriendshipStatus.NOTADDED, new ImageDTO(1, "friend3", "test"))
    ]
  }


}
