import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  constructor(private router: Router, private authService: AuthService) { }

  resetData = {
    username: '',
    securityQuestionValue: '',
    securityQuestionAnswer: '',
    password: '',
    confirmPassword: ''
  }
  errorMessage: string | null = null;

  // what happens when the user clicks submit on HTML...
  onResetPassword(){
    // checking no data is null
    if (!this.resetData.username ||
      !this.resetData.securityQuestionValue ||
      !this.resetData.securityQuestionAnswer ||
      !this.resetData.password ||
      !this.resetData.confirmPassword) {
      this.errorMessage = "Please enter all information";
      return;
    }
    // checking password and confirm password match
    if (this.resetData.password !== this.resetData.confirmPassword){
      this.errorMessage = "Password and Confirm Password Need to Match";
      return;
    }
    // already checked that it's not null
    this.authService.forgotPassword(this.resetData).subscribe(
      (response) => {
        this.errorMessage = null;
        this.router.navigate(['/change-password']);
      },
      (error) => {
        this.errorMessage = error.message;
      }
    )
    // if everything goes fine, take away error message
    this.errorMessage = null;
  }

}
