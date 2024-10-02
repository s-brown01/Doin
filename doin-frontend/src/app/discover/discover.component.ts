import { Component } from '@angular/core';
// import { HomeComponent } from '../home/home.component';
import { CommonModule } from "@angular/common";
import { EventComponent } from "../event/event.component";
import { EventService } from "../services/event.service";
import { EventDTO } from "../dtos/event.dto";

@Component({
  selector: 'app-discover',
  standalone: true,
  imports: [EventComponent, CommonModule],
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
