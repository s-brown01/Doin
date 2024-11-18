import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ErrorService } from '../services/error.service';

@Component({
  selector: 'app-error-notification',
  templateUrl: './app-error-notification.component.html',
  styleUrls: ['./app-error-notification.component.css']
})
export class ErrorNotificationComponent implements OnInit, OnDestroy {
  errorMessage: string = '';
  showError: boolean = false;
  private errorSubscription: Subscription | undefined;

  constructor(private errorService: ErrorService) {}

  ngOnInit(): void {
    this.errorSubscription = this.errorService.error$.subscribe(
      (message) => {
        this.errorMessage = message;
        this.showError = true;
        setTimeout(() => {
          this.showError = false;
        }, 10000);  // Auto-hide after 5 seconds
      }
    );
  }

  ngOnDestroy(): void {
    this.errorSubscription?.unsubscribe();
  }
}