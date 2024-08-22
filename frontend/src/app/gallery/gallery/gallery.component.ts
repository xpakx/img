import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GalleryEvent } from '../dto/gallery-event';
import { Image } from '../dto/image';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {
  @Input("choice") idForImageModal?: String = undefined;
  @Output("action") actionEvent = new EventEmitter<GalleryEvent>();
  @Input("images") images: Image[] = [];
  apiUrl = environment.apiUrl;

  constructor() { }

  ngOnInit(): void {
  }

  openImage(id: String) {
    this.actionEvent.emit({id: id, type: "Open"});
  }

  openImageNew(id: String) {
    this.actionEvent.emit({id: id, type: "OpenNew"});
  }

  closeImage() {
    this.actionEvent.emit({id: undefined, type: "Close"});
  }
}
