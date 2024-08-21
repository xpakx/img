import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { UploadService } from '../upload.service';

import { UploadComponent } from './upload.component';

describe('UploadComponent', () => {
  let uploadService: jasmine.SpyObj<UploadService>;
  let component: UploadComponent;
  let fixture: ComponentFixture<UploadComponent>;

  beforeEach(async () => {
    uploadService = jasmine.createSpyObj('UploadService', ['sendSound']);
    await TestBed.configureTestingModule({
      declarations: [ UploadComponent ],
      providers: [
        { provide: UploadService, useValue: uploadService },
        FormBuilder
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
