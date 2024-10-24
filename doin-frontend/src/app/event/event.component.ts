import { Component, Input } from '@angular/core';
import { EventDTO } from '../dtos/event.dto';
import { EventService } from '../services/event.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrl: './event.component.css',
})
export class EventComponent {
  @Input() event!: EventDTO;
  time: string = 'few moments ago'
  result: string|null = null

  constructor(private eventService: EventService, private authService: AuthService){
    this.eventService = eventService;
  }
  ngOnInit(){
    this.time = this.calculateTime()
  }

  join(){
    const currentUser = this.authService.getCurrentUser();

    if (this.event && currentUser) {
      this.eventService.joinEvent(this.event.id, currentUser.id).subscribe(
        (response) => {
          if(response){
            this.result = '🎉joined event!'
            console.log('Successfully joined event', response);
          }else{
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
