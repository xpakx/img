import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { FollowData } from './dto/follow-data';
import { FollowRequest } from './dto/follow-request';

@Injectable({
  providedIn: 'root'
})
export class FollowService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public follow(request: FollowRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/follows`, request, { headers: this.getHeaders() });
  }

  public unfollow(username: String): Observable<any> {
    return this.http.delete<any>(`${this.apiServerUrl}/follows/users/${username}`, { headers: this.getHeaders() });
  }

  public getFollows(username: String): Observable<FollowData> {
    return this.http.get<FollowData>(`${this.apiServerUrl}/user/${username}/follows`, { headers: this.getHeaders() });
  }
}
