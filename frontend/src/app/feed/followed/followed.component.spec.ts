import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowedComponent } from './followed.component';
import { RouterTestingModule } from '@angular/router/testing';
import { provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('FollowedComponent', () => {
  let component: FollowedComponent;
  let fixture: ComponentFixture<FollowedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FollowedComponent],
      imports: [RouterTestingModule],
      providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FollowedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
