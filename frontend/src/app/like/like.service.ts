import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LikeRequest } from './dto/like-request';
import { LikeData } from './dto/like-data';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public like(request: LikeRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/likes`, request, { headers: this.getHeaders() });
  }

  public unlike(imageId: String): Observable<any> {
    return this.http.delete<any>(`${this.apiServerUrl}/likes/images/${imageId}`, { headers: this.getHeaders() });
  }

  public getLikes(imageId: String): Observable<LikeData> {
    return this.http.get<LikeData>(`${this.apiServerUrl}/image/${imageId}/likes`, { headers: this.getHeaders() });
  }
}
