import { Component, Input } from '@angular/core';
import { EventDTO } from '../dtos/event.dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-event-mini',
  templateUrl: './event-mini.component.html',
  styleUrl: './event-mini.component.css'
})
export class EventMiniComponent {
  @Input() event: EventDTO | null = null; 
  constructor(private router:Router){

  }
  ngOnInit(): void {

  }
  getShortDescription(): string {
    if(this.event){
      return this.event.description.length > 18 ? this.event.description.slice(0, 15) + '...' : this.event.description;
    }
    return '';
  }
}
