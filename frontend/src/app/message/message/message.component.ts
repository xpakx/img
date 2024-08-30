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
  type: "Received" | "Sent" | "Unread" = "Unread";
  received: boolean = false;

  ngOnInit(): void {
    this.getUnread();
  }

  getUnread() {
    this.type = "Unread";
    this.received = true;
    this.privService.getUnreadMessages().subscribe({
      next: (response: Page<PrivateMessage>) => this.messages = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  getSent() {
    this.type = "Sent";
    this.received = false;
    this.privService.getSentMessages().subscribe({
      next: (response: Page<PrivateMessage>) => this.messages = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  getReceived() {
    this.type = "Received";
    this.received = true;
    this.privService.getMessages().subscribe({
      next: (response: Page<PrivateMessage>) => this.messages = response.content,
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  delete(id: number) {
    this.privService.deleteMessage(id).subscribe({
      next: (_response: any) => this.onDelete(id),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }

  onDelete(id: number) {
    this.messages = this.messages.filter((msg) => msg.id != id);
  }
}
