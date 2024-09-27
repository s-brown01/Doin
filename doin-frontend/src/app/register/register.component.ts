import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApiService } from '../services/api.service';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  constructor(private router: Router, private apiService : ApiService) { }
  registerData = {
    username: '',
    password: '',
    confirmPassword: '',
    securityQuestion: '',
    securityAnswer: ''
  }

  onRegister(){
    if (this.registerData.password != this.registerData.confirmPassword){
      console.error("Passwords need to match");
      return;
    }

    if (this.registerData.password.length < 8){
      console.error("Password must be at least 8 characters");
      return;
    }

    if (this.registerData.username && this.registerData.password) {
      this.apiService.post('register', this.registerData).subscribe(
        response => {
          if (response.ok){
            console.log('Registration successful - moving to login', response);
            this.router.navigate(['/login']);
          }
          this.router.navigate(['/login']);
        },
        error => {
          if (error.status == 401){
            console.error('Invalid username or password, please try again');
          } else if (error.status == 400) {
            console.error('Bad request. Please double check input');
          } else {
            console.error('Registration failed:', error);
          }
        }
      );
    } else {
      console.error('Please provide both username and password.');
    }

  }
}
