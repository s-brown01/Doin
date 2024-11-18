import {Component, Input, OnInit} from '@angular/core';
import {ImageDTO} from '../dtos/image.dto';


@Component({
  selector: 'app-image',
  templateUrl: './image.component.html'
})
export class ImageComponent implements OnInit {
  imageData: string | null = null;
  defaultImg: string = 'https://isobarscience-1bfd8.kxcdn.com/wp-content/uploads/2020/09/default-profile-picture1.jpg'
  @Input() image: ImageDTO | null | undefined = null;
  @Input() borderRadius: string = '0';
  @Input() imageId: number = 0;


  ngOnInit(): void {

    if (this.image?.data) {
      this.imageData = 'data:image/jpeg;base64,' + this.image?.data;
    } else {
      this.imageData = this.defaultImg;
    }


  }
}
