import { Component } from '@angular/core';
import { PrivateMessageService } from '../private-message.service';
import { PrivateMessage } from '../dto/private-message';
import { Page } from 'src/app/gallery/dto/page';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrl: './message.component.css'
})
export class MessageComponent {
  constructor(private privService: PrivateMessageService) { }
  messages: PrivateMessage[] = [];

  ngOnInit(): void {
    this.getUnread();
  }

  getUnread() {
    this.privService.getUnreadMessages().subscribe({
      next: (response: Page<PrivateMessage>) => this.messages = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
