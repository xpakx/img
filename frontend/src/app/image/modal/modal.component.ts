import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  @Input("id") id: String = "";
  @Output("close") closeEvent = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  @HostListener('document:keydown.escape', ['$event']) onKeydownHandler(_evt: KeyboardEvent) {
    this.closeEvent.emit(true);
  }
}
