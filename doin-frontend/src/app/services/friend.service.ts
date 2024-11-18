import { Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { FriendshipDto } from '../dtos/friendship.dto';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class FriendService {

  private baseUrl = 'friends'; 

  constructor(private apiService: ApiService) { }

  getFriendsOfFriends(): Observable<FriendshipDto[]> {
    return this.apiService.get(`${this.baseUrl}`);
  }

  getFriends(userId: number): Observable<FriendshipDto[]> {
    return this.apiService.get(`${this.baseUrl}/get-friends/${userId}`);
  }

  getUserByUsername(otherUsername: string): Observable<FriendshipDto[]> {
    return this.apiService.get(`${this.baseUrl}/${otherUsername}`).pipe(
      catchError(error => {
        console.error("Error getting username " + otherUsername + ": " + error.message);
        return of([]);
      })
    );
  }

  getFriendRequests(): Observable<FriendshipDto[]> {
    return this.apiService.get(`${this.baseUrl}/friend-requests`).pipe(
      catchError(error => {
        console.error("Error when getting friend requests: " + error.message);
        return of([]);
      })
    );
  }

  addFriend(friend: FriendshipDto): Observable<any> {
    return this.apiService.post(`friends/add/${friend.username}`, friend);
  }

  confirmFriend(friend: FriendshipDto): Observable<any> {
    return this.addFriend(friend);  // Assuming confirmFriend is similar to adding a friend
  }

  removeFriend(friend: FriendshipDto): Observable<any> {
    return this.apiService.delete(`friends/remove/${friend.username}`);
  }
}