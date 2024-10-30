import { Injectable } from '@angular/core';
import {catchError, Observable, of} from 'rxjs'
import {FriendshipDto} from "../dtos/friendship.dto";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { ApiService } from './api.service';
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class FriendService {

  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient, private apiService : ApiService, private authService: AuthService) { }

  getFriendsOfFriends(): Observable<FriendshipDto[]> {
    const headers = this.getHeaders();
    return this.http.get<FriendshipDto[]>(this.baseUrl + "/friends", { headers });
  }

  getUserByUsername(otherUsername: string): Observable<FriendshipDto[]> {
    if (otherUsername == this.authService.getCurrentUser().username) {
      console.warn("OtherUsername is the same as current user's username");
      return of([]);
    }
    const headers = this.getHeaders();
    return this.http.get<FriendshipDto[]>(`${this.baseUrl}/friends/${otherUsername}`, { headers }).pipe(
      catchError(error => {
        console.error("Error getting username " + otherUsername + ": " + error.message);
        return of([]);
      })
    );
  }

  getFriendRequests(): Observable<FriendshipDto[]> {
    const headers = this.getHeaders();
    return this.http.get<FriendshipDto[]>(`${this.baseUrl}/friends/friend-requests`, { headers }).pipe(
      catchError(error => {
        console.error("Error when getting friend requests: " + error.message);
        return of([]);
      })
    )
  }

  addFriend(friend : FriendshipDto): Observable<any>{
    const headers = this.getHeaders();
    return this.apiService.post(`friends/add-friend`, friend, headers );
  }

  confirmFriend(friend : FriendshipDto): Observable<any>{
    const headers = this.getHeaders();
    return this.apiService.post("confirm-friend", friend, headers);
  }

  removeFriend(friend : FriendshipDto): Observable<any> {
    const headers = this.getHeaders();
    return this.apiService.post("friends/remove-friend", friend, headers);
  }

  private getHeaders(): HttpHeaders{
    const username = this.authService.getCurrentUser().username;
    return new HttpHeaders().set('Username', username);
  }
}
