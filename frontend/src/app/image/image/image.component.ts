import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { LikeService } from 'src/app/like/like.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent implements OnInit {
  @Input() id: String = "";
  apiUrl: String = environment.apiUrl;

  constructor(private likeService: LikeService) { }

  ngOnInit(): void {
  }

  like(): void {
    this.likeService.like(this.id).subscribe({
      next: (response: any) => console.log("liked"),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
