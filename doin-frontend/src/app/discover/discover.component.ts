import { Component } from '@angular/core';
import { EventService } from "../services/event.service";
import { EventDTO } from "../dtos/event.dto";
import {FriendService} from "../services/friend.service";
import {FriendshipDto} from "../dtos/friendship.dto";

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrl: './discover.component.css'
})
export class DiscoverComponent {
  events: EventDTO[] = [];
  mayKnowList: FriendshipDto[] = [];
  mayKnowErrorMessage: string | null = null;


  constructor(private eventService: EventService, private friendService: FriendService) {}

  ngOnInit(): void {
    this.getEvents();
    this.loadMayKnowList();
  }

  loadMayKnowList(): void {
    this.friendService.getFriendsOfFriends().subscribe(
      data => {
        this.mayKnowList = data;
        this.mayKnowErrorMessage = "baseball";
      }, error => {
        this.mayKnowList = [];
        this.mayKnowErrorMessage = error.message;
      })
  }

  trackByEventID(index: number, event: any){
    return event.id;
  }

  getEvents(): void {
    this.eventService.getEvents(0, 10).subscribe((data: EventDTO[]) => {
      this.events = data;
    });
  }
}
