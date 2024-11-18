import {ErrorHandler, Injectable, Injector} from '@angular/core';
import {Router} from '@angular/router';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(private injector: Injector) {
  }

  handleError(error: any): void {
    console.error('An error occurred:', error);

    const router = this.injector.get(Router);
    router.navigate(['/error']);
  }
}
