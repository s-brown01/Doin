import { Component, Input } from '@angular/core';
import { EventDTO } from '../dtos/event.dto';

@Component({
  selector: 'app-event',
  standalone: true,
  imports: [],
  templateUrl: './event.component.html',
  styleUrl: './event.component.css',
})
export class EventComponent {
  @Input() event!: EventDTO;

}
