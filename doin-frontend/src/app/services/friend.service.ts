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
    return this.http.get<FriendshipDto[]>(this.baseUrl + "/friends");
  }

  getUserByUsername(otherUsername: string): Observable<FriendshipDto[]> {
    if (otherUsername == this.authService.getCurrentUser().username) {
      console.warn("OtherUsername is the same as current user's username");
      return of([]);
    }
    return this.http.get<FriendshipDto[]>(`${this.baseUrl}/friends/${otherUsername}`).pipe(
      catchError(error => {
        console.error("Error getting username " + otherUsername + ": " + error.message);
        return of([]);
      })
    );
  }

  getFriendRequests(): Observable<FriendshipDto[]> {
    return this.http.get<FriendshipDto[]>(`${this.baseUrl}/friends/friend-requests`).pipe(
      catchError(error => {
        console.error("Error when getting friend requests: " + error.message);
        return of([]);
      })
    )
  }

  addFriend(friend : FriendshipDto): Observable<any>{
    return this.apiService.post(`friends/add/${friend.username}`, friend );
  }

  confirmFriend(friend : FriendshipDto): Observable<any> {
    // const headers = this.getHeaders();
    // return this.apiService.post(`confirm/${friend.username}`, friend, headers);
    return this.addFriend(friend);
  }

  removeFriend(friend : FriendshipDto): Observable<any> {
    return this.apiService.delete(`friends/remove/${friend.username}`);
  }
}
