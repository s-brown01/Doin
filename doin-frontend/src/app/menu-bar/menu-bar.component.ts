import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // Import HttpClient

@Component({
  selector: 'app-menu-bar',
  standalone: true,
  imports: [],
  templateUrl: './menu-bar.component.html',
  styleUrl: './menu-bar.component.css'
})
export class MenuBarComponent {
  
  // TEST CONNECTION - NOT GOOD PRACTICE
  message: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any>('http://localhost:8080/')
      .subscribe(data => {
        this.message = data.message;
      });
  }

}
