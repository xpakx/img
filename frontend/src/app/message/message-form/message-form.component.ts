import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PrivateMessageService } from '../private-message.service';
import { PrivateMessageRequest } from '../dto/private-message-request';
import { PrivateMessage } from '../dto/private-message';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-message-form',
  templateUrl: './message-form.component.html',
  styleUrl: './message-form.component.css'
})
export class MessageFormComponent {
  messageForm: FormGroup;
  recipient?: String;

  constructor(private formBuilder: FormBuilder, private messageService: PrivateMessageService, private route: ActivatedRoute) {
    this.messageForm = this.formBuilder.group({
      content: [''],
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if(!params['to']) {
        //TODO: error
      }
      this.recipient = params['to'];
    });
  }

  send() {
    if (this.messageForm.invalid || !this.recipient) {
      return;
    }
    const request: PrivateMessageRequest = {
      recipient: this.recipient,
      content: this.messageForm.value.content,
    }
    console.log(request)

    this.messageService.sendMessage(request).subscribe({
      next: (response: PrivateMessage) => console.log("created", response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
