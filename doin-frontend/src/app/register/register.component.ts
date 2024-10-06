import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  constructor(private router: Router, private authService: AuthService) { }
  registerData = {
    username: '',
    password: '',
    confirmPassword: '',
    securityQuestion: '',
    securityAnswer: ''
  }
  errorMessage: string | null = null;


  onRegister() {
    if (this.registerData.password !== this.registerData.confirmPassword) {
      this.errorMessage = "Passwords need to match";
      return;
    }

    if (this.registerData.password.length < 8) {
      this.errorMessage = "Password must be at least 8 characters";
      return;
    }

    if (this.registerData.username && this.registerData.password) {
      this.authService.register(this.registerData).subscribe(
        (response) => {
            console.log('succesfull')
            this.router.navigate(['/login']); 
          
        },
        (error) => {
          this.errorMessage = error;
        }
      );
    } else {
      this.errorMessage = 'Please provide both username and password.';
    }
  }
}
