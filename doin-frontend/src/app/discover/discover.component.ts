import { Component } from '@angular/core';
import { EventService } from "../services/event.service";
import { EventDTO } from "../dtos/event.dto";

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrl: './discover.component.css'
})
export class DiscoverComponent {
  events: EventDTO[] = [];

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.getEvents();
  }

  trackByEventID(index: number, event: any){
    return event.id;
  }

  getEvents(): void {
    this.eventService.getEvents(0, 10).subscribe((data: EventDTO[]) => {
      this.events = data;
    });
  }
}
