import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserDTO } from '../../dtos/user.dto';
import { ImageDTO } from '../../dtos/image.dto';
import { EventDTO, EventType } from '../../dtos/event.dto';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EventService } from '../../services/event.service';

@Component({
  selector: 'app-add-post-popup',
  templateUrl: './add-post-popup.component.html',
  styleUrls: ['./add-post-popup.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class AddPostPopupComponent {  
  description: string = '';
  location: string = '';
  time: Date = new Date();
  eventTypeName: string = '';
  evetTypeId: number = 1;
  visibility: string = 'PUBLIC'; 
  creator: UserDTO = new UserDTO(2, 'leo', new ImageDTO(1, '', '')); 
  images: ImageDTO[] = [];
  joiners: UserDTO[] = [];
  createdAt: Date = new Date();

  @Input() isVisible: boolean = false;
  @Output() closeAddPost = new EventEmitter<void>();

  eventTypes = ['party', 'meeting', 'lunch'];

  constructor(private eventService : EventService) { }

  onSubmit() {
    const event = new EventDTO(
      0,
      new EventType(this.evetTypeId, this.eventTypeName),
      this.visibility,
      this.creator, 
      this.location,
      this.time,
      this.description,
      this.images,
      this.joiners,
      this.createdAt
    );
    this.eventService.addEvent(event);
    console.log(event);
    this.closePopup();
  }

  closePopup(): void {
    this.isVisible = false;
    this.closeAddPost.emit();
  }

  openPopup() {
    this.isVisible = true;
  }
}