import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LikesComponent } from './likes.component';
import { RouterTestingModule } from '@angular/router/testing';
import { provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('LikesComponent', () => {
  let component: LikesComponent;
  let fixture: ComponentFixture<LikesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LikesComponent],
      imports: [RouterTestingModule],
      providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]

    })
    .compileComponents();

    fixture = TestBed.createComponent(LikesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
