import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { User } from '../dto/user';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {
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
}
