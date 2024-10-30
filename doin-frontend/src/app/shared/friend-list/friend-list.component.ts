import {Component, Input} from '@angular/core';
import {FriendshipDto, FriendshipStatus} from "../../dtos/friendship.dto";
import {ImageDTO} from "../../dtos/image.dto";
import {FriendService} from "../../services/friend.service";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrl: './friend-list.component.css'
})
export class FriendListComponent {
  constructor(private friendService: FriendService) {
  }

  @Input() friend!: FriendshipDto;  // Marking as input so parent can pass it4

  removeFriend() {
    console.log("Removing friend: " + this.friend.username);
  }

  confirmFriend() {
    console.log("Confirming friend: " + this.friend.username);
  }

  addFriend() {
    console.log("Adding friend: " + this.friend.username);

    this.friendService.addFriend(this.friend).subscribe(
      response => {
        if (response) {
          console.log("Successfully Added friend: " + this.friend.username);
        } else {
          console.log("Unsuccessfully Added friend: " + this.friend.username);
        }
      }, error => {
        console.error("Error occurred while adding friend: " + this.friend.username);

      }
    )

  }

  protected readonly FriendshipStatus = FriendshipStatus;
}
