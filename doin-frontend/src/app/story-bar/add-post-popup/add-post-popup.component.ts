import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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
export class AddPostPopupComponent implements OnInit{  
  description: string = '';
  location: string = '';
  time: string = '';
  eventTypeName: string = '';
  evetTypeId: number = 1;
  visibility: string = 'PUBLIC'; 
  creator: UserDTO; 
  images: ImageDTO[] = [];
  joiners: UserDTO[] = [];
  createdAt: Date; 
  minTime: string ='';

  ngOnInit(): void {
    this.minTime = new Date().toISOString().slice(0, 16);
  }

  @Input() isVisible: boolean = false;
  @Output() closeAddPost = new EventEmitter<void>();

  eventTypes = ['party', 'meeting', 'lunch'];

  constructor(private eventService: EventService, private router: Router, authService: AuthService) { 
    this.createdAt = new Date(); 
    this.creator = authService.getCurrentUser();
  }

  validateTime(): void {
    if (this.time) {
      const selectedTime = new Date(this.time);
      const currentTime = new Date();

      const isSameDay = selectedTime.toDateString() === currentTime.toDateString();

      if (isSameDay && selectedTime < currentTime) {
        this.time = this.minTime;
      }
    }
  }

  async onSubmit() {
    console.log(this.createdAt);
    const event = new EventDTO(
      0,
      new EventType(this.evetTypeId, this.eventTypeName),
      this.visibility,
      this.creator, 
      this.location,
      new Date(this.time),
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