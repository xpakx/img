import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UploadService } from '../upload.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  files?: FileList;
  @ViewChild('fileselection', {static: true}) fileselectionButton?: ElementRef;

  constructor(private upload: UploadService) { }

  ngOnInit(): void {
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

  send() {
    if(!this.files) {
      return;
    }

    this.upload.sendImage(this.files).subscribe({
      next: (response: any) => console.log("uploaded", response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
