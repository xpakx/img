import { TestBed } from '@angular/core/testing';

import { PrivateMessageService } from './private-message.service';
import { HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('PrivateMessageService', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let service: PrivateMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
    });
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    service = TestBed.inject(PrivateMessageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
