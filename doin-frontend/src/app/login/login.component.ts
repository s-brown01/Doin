import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApiService } from '../services/api.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports:[FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginData = {
    username: '',
    password: ''
  };

  constructor(private router: Router, private apiService : ApiService) { }

  onLogin() {
    const { username, password } = this.loginData;
    if (username && password) {
      this.apiService.post('login', this.loginData).subscribe(
        response => {
          console.log('Login successful', response);
          this.router.navigate(['/home']);
        },
        error => {
          console.error('Login failed:', error);
        }
      );
    } else {
      console.error('Please provide both username and password.');
    }
  }
}