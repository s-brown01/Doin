import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'
import {FriendshipDto} from "../dtos/friendship.dto";
import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';
import {FriendListComponent} from "../shared/friend-list/friend-list.component";
@Injectable({
  providedIn: 'root'
})
export class FriendService {

  private baseUrl = 'http://localhost:8080/api/friends';
  constructor(private http: HttpClient, private apiService : ApiService) { }


  getFriendsOfFriends(): Observable<FriendshipDto[]> {
    return this.http.get<FriendshipDto[]>(this.baseUrl + "/friends");
  }

  getFriendByUsername(username: string): Observable<FriendshipDto[]> {
    return this.http.get<FriendshipDto[]>(`${this.baseUrl}/friends/${username}`);
  }

  addFriend(friend : FriendshipDto): Observable<any>{
    return this.apiService.post("/friends/add-friend", friend);
  }

  removeFriend(friend : FriendshipDto): Observable<any>{
    return this.apiService.post("/friends/remove-friend", friend)
  }
}
