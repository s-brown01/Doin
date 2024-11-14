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
  invalidPasswordLength: boolean = false;
  invalidPasswordMatch: boolean = false;



  onRegister() {
    if (!this.registerData.username ||
      !this.registerData.password ||
      !this.registerData.confirmPassword ||
      !this.registerData.securityQuestion ||
      !this.registerData.securityAnswer) {
      this.errorMessage = "Please enter all information";
      return;
    }

    this.invalidPasswordMatch = false;
    this.invalidPasswordLength = false;

    if (this.registerData.password !== this.registerData.confirmPassword) {
      this.invalidPasswordMatch = true;
      this.errorMessage = "Passwords need to match";
    }

    if (this.registerData.password.length < 8) {
      this.invalidPasswordLength = true;
      this.errorMessage = "Password must be at least 8 characters";
    }

    if (this.invalidPasswordLength || this.invalidPasswordMatch) {
      return;
    }

    if (this.registerData.username && this.registerData.password) {
      this.invalidPasswordMatch = false;
      this.invalidPasswordLength = false;
      this.authService.register(this.registerData).subscribe(
        (response) => {
          this.errorMessage = null;
          this.router.navigate(['/login']);
        },
        (error) => {
          console.log(error);
          if (error.status === 400){
            this.errorMessage = "Username is already taken";
            return;
          }
          this.errorMessage = error.message;
        }
      );
    } else {
      this.errorMessage = 'Please provide both username and password.';
    }
  }
}
