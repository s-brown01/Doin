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

  getEvents(): void {
    this.eventService.getEvents().subscribe((data: EventDTO[]) => {
      this.events = data;
    });
  }
}
