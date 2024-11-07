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

  getUserEvents(page: number, size: number): Observable<EventDTO[]> {
    return this.http.get<{ content: EventDTO[] }>(`${this.baseUrl}/events/users/?page=${page}&size=${size}`)
      .pipe(
        map((response: { content: any; }) => response.content)
      );
  }

  getPublicEvents(page: number, size: number): Observable<EventDTO[]> {
    return this.http.get<{ content: EventDTO[] }>(`${this.baseUrl}/events/public?page=${page}&size=${size}`)
      .pipe(
        map((response: { content: any; }) => response.content)
      );
  }

  getUpcomingEvents(): Observable<EventDTO[]> {
    return this.http.get<EventDTO[]>(`${this.baseUrl}/events/upcoming`);
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

  addImage(eventId: number, file: File): Observable<boolean>{
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.baseUrl}/events/${eventId}/images`, formData);
  }
}