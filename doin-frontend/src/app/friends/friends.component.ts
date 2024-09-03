import { Component } from '@angular/core';
import { FriendListComponent } from '../shared/friend-list/friend-list.component';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [FriendListComponent],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.css'
})
export class FriendsComponent {

}
