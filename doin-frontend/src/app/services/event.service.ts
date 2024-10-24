import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { EventDTO } from '../dtos/event.dto';
import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';
@Injectable({
  providedIn: 'root'
})
export class EventService {

  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient, private apiService : ApiService) { }

  getEvents(page: number, size: number): Observable<EventDTO[]> {
    return this.http.get<{ content: EventDTO[] }>(`${this.baseUrl}/events?page=${page}&size=${size}`)
      .pipe(
        map((response: { content: any; }) => response.content)
      );
  }

  getEvent(id: Number): Observable<EventDTO> {
    return this.http.get<EventDTO>(`${this.baseUrl}/events/${id}`);
  }

  addEvent(event : EventDTO): Observable<any>{
    return this.apiService.post("events", event);
  }

  joinEvent(eventId: number, userId: number): Observable<boolean>{
    return this.http.post<boolean>(`${this.baseUrl}/events/${eventId}/join?userId=${userId}`, {});
  }
}