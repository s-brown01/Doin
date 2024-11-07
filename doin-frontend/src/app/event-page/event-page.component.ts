import { Component, Optional } from '@angular/core';
import { EventDTO } from '../dtos/event.dto';
import { EventService } from '../services/event.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-event-page',
  templateUrl: './event-page.component.html',
  styleUrl: './event-page.component.css'
})
export class EventPageComponent {
  event: EventDTO | null = null; 
  constructor(private eventService: EventService, private route: ActivatedRoute) {}
  imageUploadAvailable = true; 

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
    const eventId = Number(this.route.snapshot.paramMap.get('id'));
      if (eventId) {
      this.eventService.getEvent(eventId).subscribe(
        (data: EventDTO) => {
          this.event = data;
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
