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
import { FollowData } from 'src/app/follow/dto/follow-data';

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
  following: boolean = false;

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
      next: (response: User) => {
        this.user = response;
        this.loadFollows(username);
      },
      error: (err: HttpErrorResponse) => console.log(err),
    });
    this.profileService.getImages(username).subscribe({
      next: (response: Page<Image>) => this.images = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  loadFollows(username: String) {
    this.followService.getFollows(username).subscribe({
      next: (response: FollowData) => this.updateFollows(response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  updateFollows(data: FollowData) {
    if(!this.user) return;
    this.user.following = data.following;
    this.user.followers = data.followers;
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
    if(this.following) this.unfollow();
    else this.follow();
  }

  follow(): void {
    if(!this.username) {
      return;
    }
    this.followService.follow({username: this.username}).subscribe({
      next: (_response: any) => this.following = true,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  unfollow(): void {
    if(!this.username) {
      return;
    }
    this.followService.unfollow(this.username).subscribe({
      next: (_response: any) => this.following = false,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
