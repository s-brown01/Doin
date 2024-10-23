import {Component, Input} from '@angular/core';
import {FriendshipDto} from "../../dtos/friendship.dto";

@Component({
  selector: 'app-small-friend-list',
  templateUrl: './small-friend-list.component.html',
  styleUrl: './small-friend-list.component.css'
})
export class SmallFriendListComponent {
  @Input() friend!: FriendshipDto;

  // protected readonly FriendshipStatus = FriendshipStatus;
}
