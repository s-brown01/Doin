import { Component } from '@angular/core';
import { EventService } from '../services/event.service';
import { EventDTO } from '../dtos/event.dto';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  hasMoreEvents: boolean = true;
  events: EventDTO[] = [];
  currentPage = 0;
  pageSize = 10;

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getEvents(this.currentPage, this.pageSize).subscribe((newEvents: EventDTO[]) => {
      if (newEvents.length > 0) {
        this.events.push(...newEvents); 
        this.currentPage++;
      } else {
        this.hasMoreEvents = false;  
      }
    });
  }

  onLoadMore(): void {
    this.loadEvents();
  }

}
