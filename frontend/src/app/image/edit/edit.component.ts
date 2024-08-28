import { Component } from '@angular/core';
import { ImageService } from '../image.service';
import { Image } from 'src/app/gallery/dto/image';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UpdateImageRequest } from '../dto/update-image-request';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrl: './edit.component.css'
})
export class EditComponent {
  updateForm: FormGroup;
  id?: String;

  constructor(private formBuilder: FormBuilder, private upload: ImageService, private route: ActivatedRoute) {
    this.updateForm = this.formBuilder.group({
      caption: [''],
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params['id'];
    });
  }

  send() {
    if (this.updateForm.invalid || !this.id) {
      return;
    }
    const request: UpdateImageRequest = {
      caption: this.updateForm.value.caption,
    }

    this.upload.editImage(this.id, request).subscribe({
      next: (response: Image) => console.log("updated", response),
      error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
