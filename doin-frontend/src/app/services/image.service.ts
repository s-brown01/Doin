import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private api: ApiService) {}

  // getImage(id: number): Observable<string> {
  //   return this.api.getString("images/" + id);
  // }
}