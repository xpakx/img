import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { LikeData } from 'src/app/like/dto/like-data';
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
  liked: boolean = true;
  likes: number = 0;

  constructor(private likeService: LikeService) { }

  ngOnInit(): void {
    this.likeService.getLikes(this.id).subscribe({
      next: (response: LikeData) => this.likes = response.likes,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  likeClick(): void {
    if(this.liked) this.unlike();
    else this.like();
  }

  like(): void {
    this.likeService.like({imageId: this.id}).subscribe({
      next: (_response: any) => this.liked = true,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  unlike(): void {
    this.likeService.unlike(this.id).subscribe({
      next: (_response: any) => this.liked = false,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
