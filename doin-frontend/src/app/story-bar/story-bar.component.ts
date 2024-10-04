import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoryPopupComponent } from './story-popup/story-popup.component';
import { AddPostPopupComponent } from './add-post-popup/add-post-popup.component';

@Component({
  selector: 'app-story-bar',
  standalone: true,
  imports: [CommonModule, StoryPopupComponent, AddPostPopupComponent],
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
