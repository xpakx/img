import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../gallery/dto/user';
import { Image } from '../gallery/dto/image';
import { Page } from '../gallery/dto/page';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public getProfile(username: String): Observable<User> {
    return this.http.get<User>(`${this.apiServerUrl}/profile/${username}`, { headers: this.getHeaders() });
  }

  public getImages(username: String, page: number = 0): Observable<Page<Image>> {
    return this.http.get<Page<Image>>(`${this.apiServerUrl}/user/${username}/images?page=${page}`, { headers: this.getHeaders() });
  }
}
