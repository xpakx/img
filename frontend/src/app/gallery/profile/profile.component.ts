import { Component, OnInit } from '@angular/core';
import { User } from '../dto/user';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { GalleryEvent } from '../dto/gallery-event';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  username?: String;
  idForImageModal?: number = undefined;
  user: User = {
    username: "Test",
    followers: 180,
    following: 260,
    posts: 80,
    description: "description description description description\n www.example.com",
  }

  constructor(private location: Location, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.username = params['name'];
    });
    this.location.onUrlChange(url => this.closeImageOnChange(url));
  }

  openImage(id: number) {
    this.idForImageModal = id;
    this.location.go(`/profile/${this.username}/image/${id}`);
  }

  openImageNew(id: number) {
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
    if (event.type == "Open") {
      this.openImage(event.id);
    } else if (event.type == "OpenNew") {
      this.openImageNew(event.id);
    } else if (event.type == "Close") {
      this.closeImage();
    }
  }
}
