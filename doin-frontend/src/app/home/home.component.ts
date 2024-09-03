import { Component } from '@angular/core';
import { StoryBarComponent } from '../story-bar/story-bar.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [StoryBarComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
