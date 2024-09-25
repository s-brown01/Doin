import { Component } from '@angular/core';
import { StoryBarComponent } from '../story-bar/story-bar.component';
import { CommonModule } from '@angular/common';
import { EventComponent } from '../event/event.component';
import { EventService } from '../services/event.service';
import { EventDTO } from '../dtos/event.dto';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [StoryBarComponent, EventComponent, CommonModule],
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
