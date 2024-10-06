import { Component } from '@angular/core';
import { EventService } from '../services/event.service';
import { EventDTO } from '../dtos/event.dto';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
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
