import {Component} from '@angular/core';
import {EventService} from "../services/event.service";
import {EventDTO} from "../dtos/event.dto";
import {FriendService} from "../services/friend.service";
import {FriendshipDto} from "../dtos/friendship.dto";

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrl: './discover.component.css'
})

export class DiscoverComponent {
  hasMoreEvents: boolean = true;
  events: EventDTO[] = [];
  currentPage = 0;
  pageSize = 6;
  mayKnowList: FriendshipDto[] = [];
  mayKnowErrorMessage: string | null = null;


  constructor(private eventService: EventService, private friendService: FriendService) {
  }

  ngOnInit(): void {
    this.loadEvents();
    this.loadMayKnowList();
  }

  loadMayKnowList(): void {
    this.friendService.getFriendsOfFriends().subscribe(
      data => {
        this.mayKnowList = data;
        this.mayKnowErrorMessage = null;
        if (this.mayKnowList.length == 0) {
          this.mayKnowErrorMessage = "No friends of friends found!";
        }
      }, error => {
        this.mayKnowList = [];
        this.mayKnowErrorMessage = error.message;
      })
  }

  trackByEventID(index: number, event: any) {
    return event.id;
  }

  loadEvents(): void {
    this.eventService.getPublicEvents(this.currentPage, this.pageSize).subscribe((newEvents: EventDTO[]) => {
      if (newEvents.length > 0) {
        this.events.push(...newEvents);
        this.currentPage++;
      } else {
        this.hasMoreEvents = false;
      }
    });
  }

  onLoadMore(): void {
    this.loadEvents();
  }
}
