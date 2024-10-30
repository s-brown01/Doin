import {Component, Input} from '@angular/core';
import {FriendshipDto, FriendshipStatus} from "../../dtos/friendship.dto";
import {ImageDTO} from "../../dtos/image.dto";
import {FriendService} from "../../services/friend.service";
import {response} from "express";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrl: './friend-list.component.css'
})
export class FriendListComponent {
  constructor(private friendService: FriendService) {
  }

  @Input() friend!: FriendshipDto;  // Marking as input so parent can pass it4
  response: string | null = null;

  removeFriend() {
    console.log("Removing friend: " + this.friend.username);
    this.response = `Rejected '${this.friend.username}'`
  }

  confirmFriend() {
    console.log("Confirming friend: " + this.friend.username);
    this.response = `Confirmed '${this.friend.username}'`
  }

  addFriend() {
    console.log("Adding friend: " + this.friend.username);

    this.friendService.addFriend(this.friend).subscribe(
      response => {
        if (response) {
          this.response = `Added '${this.friend.username}'`;
        } else {
          this.response = `Unable to add '${this.friend.username}'`;
        }
      }, error => {
        this.response = `:( Error occurred while adding '${this.friend.username}'`;
      }
    )

  }

  protected readonly FriendshipStatus = FriendshipStatus;
}
