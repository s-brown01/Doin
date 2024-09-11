import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';


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

  constructor(private router: Router) { }

  onLogin() {
    const { username, password } = this.loginData;
    if (username && password) {
      console.log('Login attempt:', this.loginData);
      this.router.navigate(['/home']);
    } else {
      console.error('Please provide both username and password.');
    }
  }
}