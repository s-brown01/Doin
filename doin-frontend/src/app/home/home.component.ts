import {AfterViewInit, Component} from '@angular/core';
import {EventService} from '../services/event.service';
import {EventDTO} from '../dtos/event.dto';
import {fromEvent, Subscription} from 'rxjs';
import {debounceTime, distinctUntilChanged, filter} from 'rxjs/operators';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit {
  hasMoreEvents: boolean = true;
  events: EventDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  loading = false;
  private scrollSubscription?: Subscription;
  private routerSubscription?: Subscription;


  constructor(
    private eventService: EventService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loadEvents();
  }

  ngAfterViewInit() {
    this.routerSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      debounceTime(100)
    ).subscribe(() => {
      this.setupScrollListener();
    });

    this.setupScrollListener();
  }

  scrollToTop() {
    const middleWrapper = document.querySelector('.middle-wrapper');
    if (middleWrapper) {
      middleWrapper.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    }
  }

  loadEvents(): void {
    if (this.loading) return;

    this.loading = true;

    this.eventService.getEvents(this.currentPage, this.pageSize).subscribe({
      next: (newEvents: EventDTO[]) => {
        if (newEvents.length > 0) {
          this.events.push(...newEvents);
          this.currentPage++;
        } else {
          this.hasMoreEvents = false;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading events:', error);
        this.loading = false;
      }
    });
  }

  private setupScrollListener() {
    if (this.scrollSubscription) {
      this.scrollSubscription.unsubscribe();
    }

    const middleWrapper = document.querySelector('.middle-wrapper');

    if (middleWrapper) {
      this.scrollSubscription = fromEvent(middleWrapper, 'scroll')
        .pipe(
          debounceTime(150),
          distinctUntilChanged()
        )
        .subscribe(() => {
          const threshold = 300;
          const {scrollTop, scrollHeight, clientHeight} = middleWrapper as HTMLElement;
          const remainingScroll = scrollHeight - (scrollTop + clientHeight);

          if (remainingScroll < threshold && !this.loading && this.hasMoreEvents) {
            this.loadEvents();
          }
        });
    }
  }
}
