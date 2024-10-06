import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-story-popup',
  templateUrl: './story-popup.component.html',
  styleUrls: ['./story-popup.component.css']
})
export class StoryPopupComponent {
  @Input() storyImageUrl: string = '';
  @Input() userAvatarUrl: string = '';
  @Input() userName: string = '';

  @Input() isVisible: boolean = false;

  @Output() close = new EventEmitter<void>();

  closePopup(): void {
    this.isVisible = false;
    this.close.emit();
  }

  openPopup() {
    this.isVisible = true;
  }

}