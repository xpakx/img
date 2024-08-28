import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { LikeService } from 'src/app/like/like.service';
import { environment } from 'src/environments/environment';
import { ImageService } from '../image.service';
import { ImageDetails } from 'src/app/gallery/dto/image-details';
import { Comment } from '../dto/comment';
import { CommentService } from 'src/app/comment/comment.service';
import { Page } from 'src/app/gallery/dto/page';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent implements OnInit {
  @Input() id: String = "";
  apiUrl: String = environment.apiUrl;
  image?: ImageDetails;
  comments: Comment[] = [];

  constructor(private imageService: ImageService, private likeService: LikeService, private commentService: CommentService) { }

  ngOnInit(): void {
    this.imageService.getImageDetails(this.id).subscribe({
      next: (response: ImageDetails) => this.image = response,
      error: (err: HttpErrorResponse) => console.log(err),
    });

    this.commentService.getComments(this.id).subscribe({
      next: (response: Page<Comment>) => this.comments = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  likeClick(): void {
    if(!this.image) return
    if(this.image.liked) this.unlike(this.image);
    else this.like(this.image);
  }

  like(image: ImageDetails): void {
    this.likeService.like({imageId: this.id}).subscribe({
      next: (_response: any) => image.liked = true,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  unlike(image: ImageDetails): void {
    this.likeService.unlike(this.id).subscribe({
      next: (_response: any) => image.liked = false,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  delete(): void {
    this.imageService.deleteImage(this.id).subscribe({
      next: (response: any) => console.log("deleted"),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
