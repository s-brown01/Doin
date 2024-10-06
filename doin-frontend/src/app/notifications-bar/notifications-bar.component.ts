import { Component } from '@angular/core';
import { ImageComponent } from '../image/image.component';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrl: './notifications-bar.component.css'
})
export class NotificationsBarComponent {
  constructor(private authService: AuthService, private router: Router){
  }

  logout(){
    this.authService.clearToken();
  }
}
