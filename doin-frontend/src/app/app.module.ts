import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { MenuBarComponent } from './menu-bar/menu-bar.component';
import { NotificationsBarComponent } from './notifications-bar/notifications-bar.component';
import { StoryBarComponent } from './story-bar/story-bar.component';
import { AuthService } from './services/auth.service';
import { ApiService } from './services/api.service';
import { ImageService } from './services/image.service';
import { EventService } from './services/event.service';
import { AuthInterceptor } from './utils/AuthInterceptor';
import { RouterModule } from '@angular/router';
import { AddPostPopupComponent } from './story-bar/add-post-popup/add-post-popup.component';
import { StoryPopupComponent } from './story-bar/story-popup/story-popup.component';
import { FormsModule } from '@angular/forms';
import { EventComponent } from './event/event.component';
import { FriendListComponent } from './shared/friend-list/friend-list.component';
import { ProfileComponent } from './profile/profile.component';
import { HomeComponent } from './home/home.component';
import { ImageComponent } from './image/image.component';
import { DiscoverComponent } from './discover/discover.component';
import { EventPageComponent } from './event-page/event-page.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { FriendsComponent } from './friends/friends.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AppRoutingModule } from './app.routes';

@NgModule({
  declarations: [
    AppComponent,
    DiscoverComponent,
    EventComponent, 
    EventPageComponent,
    ForgotPasswordComponent,
    FriendsComponent,
    HomeComponent,
    ImageComponent,
    LoginComponent,
    MenuBarComponent,
    NotificationsBarComponent,
    ProfileComponent,
    RegisterComponent,
    FriendListComponent,
    AddPostPopupComponent,
    StoryPopupComponent,
    StoryBarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot([])

  ],
  providers: [
    AuthService,
    ApiService,
    ImageService,
    EventService,
    AuthInterceptor,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}