import { Directive, EventEmitter, HostListener, Output } from '@angular/core';

@Directive({
  selector: '[middle]'
})
export class MiddleDirective {
  @Output('middle') middleclick = new EventEmitter();

  constructor() { }

  @HostListener('mouseup', ['$event'])
  middleclickEvent(event: MouseEvent) {
    if (event.button === 1) {
      this.middleclick.emit(event);
    }
  }
}