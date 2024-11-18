import {Component, Input} from '@angular/core';
import {EventDTO} from '../dtos/event.dto';
import {EventService} from '../services/event.service';
import {AuthService} from '../services/auth.service';
import {UserDTO} from '../dtos/user.dto';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrl: './event.component.css',
})
export class EventComponent {
  @Input() event!: EventDTO;
  time: string = 'few moments ago'
  result: string | null = null
  currentUser: UserDTO | null = null
  isGoing = false;
  isPast = false;

  constructor(private eventService: EventService, private authService: AuthService) {
    this.eventService = eventService;
  }

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.time = this.calculateTime()
    if ((this.event.creator.id == this.currentUser?.id || this.event.joiners.some(joiner => joiner.id === this.currentUser?.id))) {
      this.isGoing = true;
    }
    if (new Date(this.event.time).getTime() < Date.now()) {
      this.isPast = true;
    }
  }

  getShortDescription(): string {
    return this.event.description.length > 18 ? this.event.description.slice(0, 15) + '...' : this.event.description;
  }

  join() {
    if (this.event && this.currentUser) {
      this.eventService.joinEvent(this.event.id, this.currentUser.id).subscribe(
        (response) => {
          if (response) {
            // this.result = '🎉joined event!'
            this.isGoing = true
            console.log('Successfully joined event', response);
          } else {
            this.result = '🙃already joined!'
            console.log('Error joining event:', response);
          }

        },
        (error) => {
          console.error('Error joining event:', error);
        }
      );
    } else {
      console.error('Event or user is not available');
    }
  }

  calculateTime(): string {
    const createdAt = new Date(this.event.createdAt);
    const currentTime = new Date();
    const diffInMilliseconds = currentTime.getTime() - createdAt.getTime();

    const diffInMinutes = Math.floor(diffInMilliseconds / 60000);
    const diffInHours = Math.floor(diffInMinutes / 60);
    const diffInDays = Math.floor(diffInHours / 24);

    if (diffInMinutes < 60) {
      return `${diffInMinutes} min ago`;
    } else if (diffInHours < 24) {
      return `${diffInHours} hours ago`;
    } else {
      return `${diffInDays} days ago`;
    }
  }

}
