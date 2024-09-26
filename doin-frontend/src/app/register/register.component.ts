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
          console.log('Login successful', response);
          this.router.navigate(['/login']);
        },
        error => {
          console.error('Register failed:', error);
        }
      );
    } else {
      console.error('Please provide both username and password.');
    }

  }
}
