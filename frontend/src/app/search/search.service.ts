import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Page } from '../gallery/dto/page';
import { SearchResult } from './dto/search-result';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public searchProfile(query: String, page: number = 0, short: boolean = false): Observable<Page<SearchResult>> {
    return this.http.get<Page<SearchResult>>(`${this.apiServerUrl}/search?query=${query}&page=${page}${short ? "&short=true" : ""}`, { headers: this.getHeaders() });
  }
}
