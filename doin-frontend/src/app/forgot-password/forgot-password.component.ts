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
    securityQuestion: '',
    securityAnswer: ''
  }
  errorMessage: string | null = null;

  // what happens when the user clicks submit on HTML...
  onResetPassword(){
    // checking no data is null
    if (!this.resetData.username ||
      !this.resetData.securityQuestion ||
      !this.resetData.securityAnswer ) {
      this.errorMessage = "Please enter all information";
      return;
    }

    this.errorMessage = null;


  }

}
