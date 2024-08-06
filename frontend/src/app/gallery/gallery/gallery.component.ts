import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GalleryEvent } from '../dto/gallery-event';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {
  @Input("choice") idForImageModal?: number = undefined;
  @Output("action") actionEvent = new EventEmitter<GalleryEvent>();

  constructor() { }

  ngOnInit(): void {
  }

  openImage(id: number) {
    this.actionEvent.emit({id: id, type: "Open"});
  }

  openImageNew(id: number) {
    this.actionEvent.emit({id: id, type: "OpenNew"});
  }

  closeImage() {
    this.actionEvent.emit({id: -1, type: "Close"});
  }
}
