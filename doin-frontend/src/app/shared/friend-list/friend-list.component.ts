import {Component, Input} from '@angular/core';
import {FriendshipDto, FriendshipStatus} from "../../dtos/friendship.dto";
import {ImageDTO} from "../../dtos/image.dto";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrl: './friend-list.component.css'
})
export class FriendListComponent {
  @Input() friend!: FriendshipDto;  // Marking as input so parent can pass it4

  removeFriend(){
    console.log("Removing friend: " + this.friend.username);
  }

  confirmFriend(){
    console.log("Confirming friend: " + this.friend.username);
  }

  addFriend(){
    console.log("Adding friend: " + this.friend.username);
  }

  protected readonly FriendshipStatus = FriendshipStatus;
}
