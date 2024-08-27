import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Image } from '../gallery/dto/image';
import { Page } from '../gallery/dto/page';

@Injectable({
  providedIn: 'root'
})
export class FeedService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public getLikes(page: number = 0): Observable<Page<Image>> {
    return this.http.get<Page<Image>>(`${this.apiServerUrl}/likes?page=${page}`, { headers: this.getHeaders() });
  }
}
