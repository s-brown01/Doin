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
    securityQuestionAnswer: ''
  }
  errorMessage: string | null = null;

  // what happens when the user clicks submit on HTML...
  onResetPassword(){
    // checking no data is null
    if (!this.resetData.username ||
      !this.resetData.securityQuestionValue ||
      !this.resetData.securityQuestionAnswer ) {
      this.errorMessage = "Please enter all information";
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


    this.errorMessage = null;


  }

}
