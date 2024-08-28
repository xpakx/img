import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CommentService } from '../comment.service';
import { Comment } from 'src/app/image/dto/comment';
import { HttpErrorResponse } from '@angular/common/http';
import { CommentRequest } from '../dto/comment-request';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {
  commentForm: FormGroup;
  @Input() id: String = "";

  constructor(private formBuilder: FormBuilder, private commentService: CommentService) {
    this.commentForm = this.formBuilder.group({
      content: [''],
    });
  }

  ngOnInit(): void { }

  send() {
    if (this.commentForm.invalid || !this.id) {
      return;
    }
    const request: CommentRequest = {
      content: this.commentForm.value.content,
    }
    console.log(request)

    this.commentService.addComment(this.id, request).subscribe({
      next: (response: Comment) => console.log("created", response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
