import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoryPopupComponent } from './story-popup/story-popup.component';

@Component({
  selector: 'app-story-bar',
  standalone: true,
  imports: [CommonModule, StoryPopupComponent],
  templateUrl: './story-bar.component.html',
  styleUrl: './story-bar.component.css'
})
export class StoryBarComponent {
  storyImageUrl: string = '';
  userAvatarUrl: string = '';
  userName: string = '';

  isStoryVisible: boolean = false;

  openStory(imageUrl: string, userName: string, userAvatarUrl: string) {
    this.storyImageUrl = imageUrl;
    this.userName = userName;
    this.togglePopup(true);
    this.userAvatarUrl = userAvatarUrl;
  }

  togglePopup(visible: boolean): void {
    this.isStoryVisible = visible;
  }
}
