import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EventDTO } from '../dtos/event.dto';
import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';
@Injectable({
  providedIn: 'root'
})
export class EventService {

  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient, private apiService : ApiService) { }

  getEvents(): Observable<EventDTO[]> {
    return this.http.get<EventDTO[]>(this.baseUrl + "/events");
  }

  addEvent(event : EventDTO){
    return this.apiService.post("events", event).subscribe(
      response => {
        console.log('Uppload successful', response);
      },
      error => {
        console.error('Uppload failed:', error);
      }
    );
  }
}