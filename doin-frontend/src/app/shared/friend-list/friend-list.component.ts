import { Component } from '@angular/core';
import {FriendshipDto} from "../../dtos/friendship.dto";
import {ImageDTO} from "../../dtos/image.dto";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrl: './friend-list.component.css'
})
export class FriendListComponent {
  friends: FriendshipDto[] = [];
  friendData = {
    username: '',
    profilePic: ImageDTO
  }



  // constructor(username: string, profilePic ImageDTO) {
  //
  // }

}
