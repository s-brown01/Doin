import { Component } from '@angular/core';

@Component({
  selector: 'app-story-bar',
  templateUrl: './story-bar.component.html',
  styleUrl: './story-bar.component.css'
})
export class StoryBarComponent {
  storyImageUrl: string = '';
  userAvatarUrl: string = '';
  userName: string = '';
  isAddPostVisible: boolean = false;
  isStoryVisible: boolean = false;

  openStory(imageUrl: string, userName: string, userAvatarUrl: string) {
    this.storyImageUrl = imageUrl;
    this.userName = userName;
    this.toggleStoryPopup(true);
    this.userAvatarUrl = userAvatarUrl;
  }

  toggleStoryPopup(visible: boolean): void {
    this.isStoryVisible = visible;
  }

  toggleAddPostPopup(visible: boolean): void {
    this.isAddPostVisible = visible;
  }
}
