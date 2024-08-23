import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

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

  public like(imageId: String): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/likes`, {"imageId": imageId}, { headers: this.getHeaders() });
  }

  public unlike(imageId: String): Observable<any> {
    return this.http.delete<any>(`${this.apiServerUrl}/likes/images/${imageId}`, { headers: this.getHeaders() });
  }
}
