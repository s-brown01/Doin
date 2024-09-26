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


  onRegister() {
    if (this.registerData.username && this.registerData.password && this.registerData.password == this.registerData.confirmPassword) {
      this.apiService.post('register', this.registerData).subscribe(
        response => {
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
