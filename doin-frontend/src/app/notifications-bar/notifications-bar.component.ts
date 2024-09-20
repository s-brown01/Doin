import { Component } from '@angular/core';
import { ImageComponent } from '../image/image.component';

@Component({
  selector: 'app-notifications-bar',
  standalone: true,
  imports: [ImageComponent],
  templateUrl: './notifications-bar.component.html',
  styleUrl: './notifications-bar.component.css'
})
export class NotificationsBarComponent {

}
