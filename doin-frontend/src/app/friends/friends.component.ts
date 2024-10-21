import { Component } from '@angular/core';
import { FriendshipDto } from "../dtos/friendship.dto";
import { FriendListComponent } from "../shared/friend-list/friend-list.component";
import { FriendService} from "../services/friend.service";
import {ImageDTO} from "../dtos/image.dto";

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent {
  friendsList: FriendshipDto[] = [];

  constructor(private friendService: FriendService) {
  }

  ngOnInit(): void {
    this.getFriends();
  }

  getFriends(): void {
    // this.friendService.getFriends().subscribe((data: FriendshipDto[]) => {
    //   this.friendsList = data;
    // });
    this.friendsList = [
      new FriendshipDto("friend1", "CONFIRMED", new ImageDTO(1, "friend1", "test")),
      new FriendshipDto("friend2", "CONFIRMED", new ImageDTO(1, "friend2", "test")),
      new FriendshipDto("friend3", "CONFIRMED", new ImageDTO(1, "friend3", "test"))
    ]
  }

  // trackByFriendID(index: number, friend: any) {
  //   return friend.id;
  // }

}
