import {HttpEventType} from '@angular/common/http';
import {Component, Input} from '@angular/core';
import {finalize} from 'rxjs';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-image-upload-modal',
  templateUrl: './app-image-upload-modal.component.html',
  styleUrl: './app-image-upload-modal.component.css'
})
export class ImageUploadModalComponent {
  isModalOpen = false;
  @Input() titile: string = 'upload image'
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  uploading = false;
  uploadProgress = 0;
  uploadMessage = '';
  uploadSuccess = false;

  constructor(private userService: UserService) {
  }

  openModal(): void {
    this.isModalOpen = true;
    document.body.style.overflow = 'hidden';
  }

  closeModal(): void {
    if (!this.uploading) {
      this.isModalOpen = false;
      this.resetModal();
      document.body.style.overflow = 'auto';
    }
    location.reload();
  }

  resetModal(): void {
    this.selectedFile = null;
    this.previewUrl = null;
    this.uploadProgress = 0;
    this.uploadMessage = '';
    this.uploadSuccess = false;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      this.createImagePreview();
      this.uploadMessage = '';
      this.uploadProgress = 0;
    }
  }

  createImagePreview(): void {
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewUrl = e.target.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  uploadFile(): void {
    if (!this.selectedFile) {
      this.uploadMessage = 'Please select a file first';
      this.uploadSuccess = false;
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.uploading = true;
    this.uploadMessage = '';

    // Call the UserService to upload the file
    this.userService.uploadProfileImage(formData).pipe(
      finalize(() => {
        this.uploading = false;
      })
    ).subscribe({
      next: (event: any) => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          this.uploadProgress = Math.round(100 * event.loaded / event.total);
        } else if (event.type === HttpEventType.Response) {
          this.uploadMessage = 'Upload successful!';
          this.uploadSuccess = true;
          setTimeout(() => this.closeModal(), 1500);
        }
      },
      error: (error) => {
        this.uploadMessage = 'Upload failed: ' + error.message;
        this.uploadSuccess = false;
        this.uploadProgress = 0;
      }
    });
  }
}
