import { TestBed } from '@angular/core/testing';

import { CommentService } from './comment.service';
import { HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CommentService', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let service: CommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
    });
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    service = TestBed.inject(CommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
