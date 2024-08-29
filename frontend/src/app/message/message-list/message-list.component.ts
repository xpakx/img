import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PrivateMessage } from '../dto/private-message';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrl: './message-list.component.css'
})
export class MessageListComponent {
  apiUrl: String = environment.apiUrl;
  @Input() messages: PrivateMessage[] = [];
  @Output("delete") deleteEvent = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  delete(id: number) {
    this.deleteEvent.emit(id);
  }
}
