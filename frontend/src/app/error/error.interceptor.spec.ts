import { HttpClient, HttpErrorResponse, HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { AuthResponse } from '../auth/dto/auth-response';

import { ErrorInterceptor } from './error.interceptor';

describe('ErrorInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['refreshToken']);

    TestBed.configureTestingModule({
    imports: [],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
        { provide: AuthService, useValue: authServiceSpy },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
    ]
});

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should pass through non-error responses', () => {
    const mockData = { success: true };

    httpClient.get('/test').subscribe(response => {
      expect(response).toEqual(mockData);
    });

    const req = httpMock.expectOne('/test');
    expect(req.request.method).toBe('GET');
    req.flush(mockData);
  });

  it('should refresh token and retry the request on 401 error', () => {
    const mockRefreshResponse: AuthResponse = {
      token: 'newToken',
      refresh_token: 'newRefreshToken',
      username: 'testUser',
      moderator_role: false,
    };

    localStorage.setItem('refresh', 'oldRefreshToken');
    authService.refreshToken.and.returnValue(of(mockRefreshResponse));

    httpClient.get('/protected').subscribe(response => {
      expect(response).toEqual({ success: true });
    });

    const initialRequest = httpMock.expectOne('/protected');
    initialRequest.flush(null, { status: 401, statusText: 'Unauthorized' });

    const retriedRequest = httpMock.expectOne('/protected');
    expect(retriedRequest.request.headers.get('Authorization')).toBe('Bearer newToken');
    retriedRequest.flush({ success: true });
  });

  it('should clear storage and throw error if no refresh token is available on 401 error', () => {
    localStorage.removeItem('refresh');
    spyOn(localStorage, 'removeItem');

    httpClient.get('/protected').subscribe({
      next: () => fail('Should have failed with 401 error'),
      error: (error: HttpErrorResponse) => {
        expect(error.status).toBe(401);
      }
    });

    const req = httpMock.expectOne('/protected');
    req.flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(localStorage.removeItem).toHaveBeenCalledWith('refresh');
    expect(localStorage.removeItem).toHaveBeenCalledWith('token');
    expect(localStorage.removeItem).toHaveBeenCalledWith('username');
  });

  it('should pass through non-401 errors', () => {
    httpClient.get('/data').subscribe({
      next: () => fail('Should have failed with 500 error'),
      error: (error: HttpErrorResponse) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpMock.expectOne('/data');
    req.flush(null, { status: 500, statusText: 'Server Error' });
  });
});
