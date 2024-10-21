import { Component } from '@angular/core';
import { FriendshipDto } from "../dtos/friendship.dto";
import { FriendListComponent } from "../shared/friend-list/friend-list.component";
import { FriendService} from "../services/friend.service";

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
    this.friendService.getFriends().subscribe((data: FriendshipDto[]) => {
      this.friendsList = data;
    });
  }

  // trackByFriendID(index: number, friend: any) {
  //   return friend.id;
  // }

}
