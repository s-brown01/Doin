import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EventDTO } from '../dtos/event.dto';
import { HttpClient } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class EventService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getEvents(): Observable<EventDTO[]> {
    return this.http.get<EventDTO[]>(this.baseUrl + "/events");
  }
}