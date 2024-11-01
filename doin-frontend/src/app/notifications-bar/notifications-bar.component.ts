import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UserDTO } from '../dtos/user.dto';

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrl: './notifications-bar.component.css'
})
export class NotificationsBarComponent {
  user: UserDTO | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.currentUser.subscribe((user: UserDTO | null) => {
      this.user = user;
    });
  }
  goToProfile(): void {
    if (this.user) {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/user', this.user?.id]);
      });
    }
  }

  logout(){
    this.authService.clearToken();
  }
}
