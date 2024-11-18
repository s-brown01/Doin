import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { EventDTO } from '../dtos/event.dto';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private baseUrl = 'events';  // Base URL is now simplified

  constructor(private apiService: ApiService) { }

  // Use ApiService to handle HTTP GET requests
  getEvents(page: number, size: number): Observable<EventDTO[]> {
    return this.apiService.get(`${this.baseUrl}?page=${page}&size=${size}`)
      .pipe(
        map((response: { content: EventDTO[] }) => response.content)
      );
  }

  getUserEvents(userId: number, page: number, size: number): Observable<EventDTO[]> {
    return this.apiService.get(`${this.baseUrl}/users/${userId}?page=${page}&size=${size}`)
      .pipe(
        map((response: { content: EventDTO[] }) => response.content)
      );
  }

  getPublicEvents(page: number, size: number): Observable<EventDTO[]> {
    return this.apiService.get(`${this.baseUrl}/public?page=${page}&size=${size}`)
      .pipe(
        map((response: { content: EventDTO[] }) => response.content)
      );
  }

  getUpcomingEvents(): Observable<EventDTO[]> {
    return this.apiService.get(`${this.baseUrl}/upcoming`);
  }

  getEvent(id: Number): Observable<EventDTO> {
    return this.apiService.get(`${this.baseUrl}/${id}`);
  }

  addEvent(event: EventDTO): Observable<any> {
    return this.apiService.post("events", event);
  }

  joinEvent(eventId: number, userId: number): Observable<boolean> {
    return this.apiService.post(`events/${eventId}/join?userId=${userId}`, {});
  }

  addImage(eventId: number, file: File): Observable<boolean> {
    const formData = new FormData();
    formData.append('file', file);
    return this.apiService.postFormData(`events/${eventId}/images`, formData);
  }
}