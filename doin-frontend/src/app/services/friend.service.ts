import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'
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

  getFriendByUsername(username: string): Observable<FriendshipDto[]> {
    return this.http.get<FriendshipDto[]>(`${this.baseUrl}/friends/${username}`);
  }

  addFriend(friend : FriendshipDto): Observable<any>{
    const headers = this.getHeaders();
    return this.apiService.post("/friends/add-friend", friend, headers );
  }

  removeFriend(friend : FriendshipDto): Observable<any>{
    const headers = this.getHeaders();
    return this.apiService.post("/friends/remove-friend", friend, headers);
  }

  blockUser(friend : FriendshipDto): Observable<any>{
    const headers = this.getHeaders();
    return this.apiService.post("/friends/block-friend", friend, headers);
  }

  unblockUser(friend : FriendshipDto): Observable<any>{
    const headers = this.getHeaders();
    return this.apiService.post("/friends/unblock-friend", friend, headers);
  }

  private getHeaders(): HttpHeaders{
    const username = this.authService.getCurrentUser().username;
    return new HttpHeaders().set('Username', username);
  }
}
