import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ImageService } from '../services/image.service';


@Component({
  selector: 'app-image',
  templateUrl: './image.component.html'
})
export class ImageComponent implements OnInit {
  imageData: string | null = null;
  @Input() borderRadius: string = '0';

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {
    const imageId = 2;
    this.imageService.getImage(imageId).subscribe((data: string) => {
      this.imageData = 'data:image/jpeg;base64,' + data;
    });
  }

  convertStringToBase64(byteArrayString: string): string {
    const byteArray = byteArrayString.split(',').map(Number);
    const base64String = btoa(
      String.fromCharCode(...new Uint8Array(byteArray))
    );

    return 'data:image/jpeg;base64,' + base64String;
  }
}