import {Component, Input} from '@angular/core';
import {FriendshipDto, FriendshipStatus} from "../../dtos/friendship.dto";
import {FriendService} from "../../services/friend.service";
import {Router} from '@angular/router';


@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrl: './friend-list.component.css'
})
export class FriendListComponent {
  @Input() friend!: FriendshipDto;  // Marking as input so parent can pass it4
  @Input() response: string | null = null;
  protected readonly FriendshipStatus = FriendshipStatus;

  constructor(private friendService: FriendService, private router: Router) {
  }

  removeFriend() {
    console.log("Removing friend: " + this.friend.username);
    this.response = `Removed '${this.friend.username}'`

    this.friendService.removeFriend(this.friend).subscribe(
      response => {
        if (response) {
          this.response = `Removed '${this.friend.username}'`
        } else {
          this.response = `Unable to remove '${this.friend.username}'`;
        }
      }, error => {
        this.response = `:( Error occurred while removing '${this.friend.username}'`;
      }
    )
  }

  confirmFriend() {
    console.log("Confirming friend: " + this.friend.username);

    this.friendService.confirmFriend(this.friend).subscribe(
      response => {
        if (response) {
          this.response = `Confirmed '${this.friend.username}'`
        } else {
          this.response = `Unable to confirm '${this.friend.username}'`;
        }
      }, error => {
        this.response = `:( Error occurred while confirming '${this.friend.username}'`;
      }
    )
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

  goToProfile(userId: number): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['/user', this.friend?.id]);
    });

  }
}
