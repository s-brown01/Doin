import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserDTO } from '../../dtos/user.dto';
import { ImageDTO } from '../../dtos/image.dto';
import { EventDTO, EventType } from '../../dtos/event.dto';
import { EventService } from '../../services/event.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-add-post-popup',
  templateUrl: './add-post-popup.component.html',
  styleUrls: ['./add-post-popup.component.css']
})
export class AddPostPopupComponent {  
  description: string = '';
  location: string = '';
  time: Date = new Date();
  eventTypeName: string = '';
  evetTypeId: number = 1;
  visibility: string = 'PUBLIC'; 
  creator: UserDTO; 
  images: ImageDTO[] = [];
  joiners: UserDTO[] = [];
  createdAt: Date; 

  @Input() isVisible: boolean = false;
  @Output() closeAddPost = new EventEmitter<void>();

  eventTypes = ['party', 'meeting', 'lunch'];

  constructor(private eventService: EventService, private router: Router, authService: AuthService) { 
    this.createdAt = new Date(); 
    this.creator = authService.getCurrentUser();
  }

  async onSubmit() {
    console.log(this.createdAt);
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
  
    this.eventService.addEvent(event).subscribe({
      next: () => {
        this.closePopup();
        location.reload();
      },
      error: (err) => console.error("Error while adding event:", err)
    });
  }

  closePopup(): void {
    this.isVisible = false;
    this.closeAddPost.emit();
  }

  openPopup() {
    this.isVisible = true;
  }
}