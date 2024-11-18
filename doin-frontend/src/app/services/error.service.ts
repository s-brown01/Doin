import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorService{

  private errorSubject = new Subject<string>();
  error$ = this.errorSubject.asObservable();

  constructor() {}

  notifyError(message: string): void {
    this.errorSubject.next(message);
  }
}
