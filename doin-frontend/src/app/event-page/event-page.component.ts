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
