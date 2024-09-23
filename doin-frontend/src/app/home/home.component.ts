import { Component } from '@angular/core';
import { StoryBarComponent } from '../story-bar/story-bar.component';
import { EventComponent } from '../event/event.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [StoryBarComponent, EventComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
