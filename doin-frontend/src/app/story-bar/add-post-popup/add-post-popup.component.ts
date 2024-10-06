import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserDTO } from '../../dtos/user.dto';
import { ImageDTO } from '../../dtos/image.dto';
import { EventDTO, EventType } from '../../dtos/event.dto';
import { EventService } from '../../services/event.service';
import { Router } from '@angular/router';
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
  creator: UserDTO = new UserDTO(2, 'leo', new ImageDTO(1, '', '')); 
  images: ImageDTO[] = [];
  joiners: UserDTO[] = [];
  createdAt: Date = new Date();

  @Input() isVisible: boolean = false;
  @Output() closeAddPost = new EventEmitter<void>();

  eventTypes = ['party', 'meeting', 'lunch'];

  constructor(private eventService : EventService, private router: Router) { }

  async onSubmit() {
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
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate([this.router.url]);
        });
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