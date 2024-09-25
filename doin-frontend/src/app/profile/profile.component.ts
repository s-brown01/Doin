import { Component } from '@angular/core';
import { ImageComponent } from '../image/image.component';
import { FriendListComponent } from '../shared/friend-list/friend-list.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ImageComponent, FriendListComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

}
