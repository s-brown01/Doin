import {Component} from '@angular/core';
import {ApiService} from '../services/api.service';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-login',
  // standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage: string | null = null;

  loginData = {
    username: '',
    password: ''
  };

  constructor(private router: Router, private apiService: ApiService, private authService: AuthService) {
  }

  onLogin() {
    const {username, password} = this.loginData;
    if (username && password) {
      this.authService.login(this.loginData).subscribe(
        (response) => {
          this.router.navigate(['/home']);
        },
        (error) => {
          if (error.status === 401) {
            this.errorMessage = "Invalid username or password. Please try again!";
          } else {
            this.errorMessage = error;
          }
        }
      );
    }
  }
}
