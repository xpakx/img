import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImagePageComponent } from './image-page.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ImagePageComponent', () => {
  let component: ImagePageComponent;
  let fixture: ComponentFixture<ImagePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImagePageComponent ],
      imports: [RouterTestingModule, HttpClientTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImagePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
