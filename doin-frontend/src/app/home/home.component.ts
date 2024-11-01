import { Component, ElementRef, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { EventService } from '../services/event.service';
import { EventDTO } from '../dtos/event.dto';
import { fromEvent, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit, OnDestroy {
  @ViewChild('contentWrapper') contentWrapper!: ElementRef;
  
  hasMoreEvents: boolean = true;
  events: EventDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  loading = false;
  private scrollSubscription?: Subscription;
  private routerSubscription?: Subscription;
  private middleWrapper: HTMLElement | null = null;

  constructor(
    private eventService: EventService,
    private router: Router
  ) {}

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

  private setupScrollListener() {
    if (this.scrollSubscription) {
      this.scrollSubscription.unsubscribe();
    }

    this.middleWrapper = document.querySelector('.middle-wrapper');
    
    if (this.middleWrapper) {
      
      this.scrollSubscription = fromEvent(this.middleWrapper, 'scroll')
        .pipe(
          debounceTime(150),
          distinctUntilChanged()
        )
        .subscribe(() => {
          const threshold = 300;
          const { scrollTop, scrollHeight, clientHeight } = this.middleWrapper as HTMLElement;
          const remainingScroll = scrollHeight - (scrollTop + clientHeight);
          
          if (remainingScroll < threshold && !this.loading && this.hasMoreEvents) {
            this.loadEvents();
          }
        });
    } else {
    }
  }

  scrollToTop() {
    if (this.middleWrapper) {
      this.middleWrapper.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    }
  }

  ngOnDestroy() {
    if (this.scrollSubscription) {
      this.scrollSubscription.unsubscribe();
    }
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
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
}