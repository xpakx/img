import { Component } from '@angular/core';
import { Image } from 'src/app/gallery/dto/image';
import { FeedService } from '../feed.service';
import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { GalleryEvent } from 'src/app/gallery/dto/gallery-event';
import { Page } from 'src/app/gallery/dto/page';

@Component({
  selector: 'app-likes',
  templateUrl: './likes.component.html',
  styleUrl: './likes.component.css'
})
export class LikesComponent {
  idForImageModal?: String = undefined;
  images: Image[] = [];
  username?: String;

  constructor(private location: Location, private feedService: FeedService) { }

  ngOnInit(): void {
    this.loadLikes();
    this.location.onUrlChange(url => this.closeImageOnChange(url));
  }

  loadLikes(): void {
    this.feedService.getLikes().subscribe({
      next: (response: Page<Image>) => this.images = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  openImage(id: String) {
    this.idForImageModal = id;
    this.location.go(`/profile/${this.username}/image/${id}`);
  }

  openImageNew(id: String) {
    let url = `/profile/${this.username}/image/${id}`;
    window.open(url, "_blank");
  }

  closeImage() {
    this.idForImageModal = undefined;
    this.location.go(`/profile/${this.username}`);
  }

  closeImageOnChange(_url: String) {
    if (this.location.isCurrentPathEqualTo(`/likes`)) {
      this.idForImageModal = undefined;
    }
  }

  onGalleryEvent(event: GalleryEvent) {
    if (event.type == "Open" && event.id) {
      this.openImage(event.id);
    } else if (event.type == "OpenNew" && event.id) {
      this.openImageNew(event.id);
    } else if (event.type == "Close") {
      this.closeImage();
    }
  }
}
