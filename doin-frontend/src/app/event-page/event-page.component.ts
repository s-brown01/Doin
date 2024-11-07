import { Component, Optional } from '@angular/core';
import { EventDTO } from '../dtos/event.dto';
import { EventService } from '../services/event.service';
import { ActivatedRoute } from '@angular/router';
import { UserDTO } from '../dtos/user.dto';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-event-page',
  templateUrl: './event-page.component.html',
  styleUrl: './event-page.component.css'
})
export class EventPageComponent {
  event: EventDTO | null = null; 
  constructor(private eventService: EventService, private route: ActivatedRoute, private authService: AuthService) {}
  imageUploadAvailable = false; 
  curUser: UserDTO | null = null;

  onImageUpload(e: Event) {
    const fileInput = e.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0] && this.event) {
      const file = fileInput.files[0];
      this.eventService.addImage(this.event?.id, file).subscribe(
        (response) => {
          if(response){
            location.reload();
            console.log('succsfuly added')
          }else{
            console.log('Error adding image:', response);
          }
          
        },
        (error) => {
          console.error('Error joining event:', error);
        }
      );
      console.log('Image uploaded:', file);
    }

  }
  ngOnInit(): void {
    this.authService.currentUser.subscribe((usr: UserDTO | null) => {
      this.curUser = usr;
    });
    const eventId = Number(this.route.snapshot.paramMap.get('id'));
      if (eventId) {
      this.eventService.getEvent(eventId).subscribe(
        (data: EventDTO) => {
          this.event = data;
          if(new Date(this.event.time).getTime() < Date.now() && (this.event.creator.id == this.curUser?.id || this.event.joiners.some(joiner => joiner.id === this.curUser?.id))){
            this.imageUploadAvailable = true;
          }
        },
        (error) => {
          console.error('Error fetching event:', error);
        }
      );
    } else {
      console.error('Invalid event ID');
    }
  }
}
