import { Component, OnInit } from '@angular/core';
import { User } from '../dto/user';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { GalleryEvent } from '../dto/gallery-event';
import { ProfileService } from 'src/app/profile/profile.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Page } from '../dto/page';
import { Image } from '../dto/image';
import { FollowService } from 'src/app/follow/follow.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  username?: String;
  idForImageModal?: String = undefined;
  user?: User;
  images: Image[] = [];

  constructor(private location: Location, private route: ActivatedRoute, private profileService: ProfileService, private followService: FollowService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.username = params['name'];
      if(this.username) this.loadProfile(this.username);
    });
    this.location.onUrlChange(url => this.closeImageOnChange(url));
  }

  loadProfile(username: String): void {
    this.profileService.getProfile(username).subscribe({
      next: (response: User) => this.user = response,
      error: (err: HttpErrorResponse) => console.log(err),
    });
    this.profileService.getImages(username).subscribe({
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
    if (this.location.isCurrentPathEqualTo(`/profile/${this.username}`)) {
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

  followClick(): void {
    if (!this.user) return;
    if(this.user.followed) this.unfollow(this.user);
    else this.follow(this.user);
  }

  follow(user: User): void {
    this.followService.follow({username: user.username}).subscribe({
      next: (_response: any) => user.followed = true,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  unfollow(user: User): void {
    this.followService.unfollow(user.username).subscribe({
      next: (_response: any) => user.followed = false,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
