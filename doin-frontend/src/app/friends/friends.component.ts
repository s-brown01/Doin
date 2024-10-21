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
    // this.friendService.getFriends().subscribe((data: FriendshipDto[]) => {
    //   this.friendsList = data;
    // });
    this.friendsList = [
      // new FriendshipDto("friend1", "CONFIRMED", {url: 'pic1.jpg'}),
      // new FriendshipDto("friend2", "PENDING", {url: 'pic2.jpg'}),
      // new FriendshipDto("friend3", "CONFIRMED", {url: 'pic3.jpg'})
    ]
  }

  // trackByFriendID(index: number, friend: any) {
  //   return friend.id;
  // }

}
