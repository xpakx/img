import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { User } from 'src/app/gallery/dto/user';
import { UploadService } from 'src/app/image/upload.service';
import { ProfileService } from '../profile.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  files?: FileList;
  @ViewChild('fileselection', {static: true}) fileselectionButton?: ElementRef;
  profile?: User;
  apiUrl: String = environment.apiUrl;

  constructor(private upload: UploadService, private profileService: ProfileService) { }

  ngOnInit(): void {
    let username = localStorage.getItem("username");
    if(!username) {
      return
    }

    this.profileService.getProfile(username).subscribe({
      next: (response: User) => this.profile = response,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  selectFile(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if(fileList && fileList.length > 0) {
      this.files = fileList;
    }
  }

  openFileSelection() {
    if(this.fileselectionButton) {
      this.fileselectionButton.nativeElement.click();
    }
  }

  sendAvatar() {
    if(!this.files || !this.profile) {
      return;
    }

    this.upload.sendAvatar(this.files).subscribe({
      next: (response: any) => console.log("uploaded", response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  deleteAvatar() {
    this.upload.deleteAvatar().subscribe({
      next: (response: any) => console.log("deleted", response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
