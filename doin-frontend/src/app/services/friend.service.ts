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

  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient, private apiService : ApiService) { }


  getFriends(): Observable<FriendshipDto[]> {
    return this.http.get<FriendshipDto[]>(this.baseUrl + "/friends");
  }

  getFriendByID(id: Number): Observable<FriendshipDto> {
    return this.http.get<FriendshipDto>(`${this.baseUrl}/friends/${id}`);
  }

  addFriend(friend : FriendshipDto): Observable<any>{
    return this.apiService.post("/friends", friend);
  }
}
