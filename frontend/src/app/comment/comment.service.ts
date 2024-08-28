import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Page } from '../gallery/dto/page';
import { Comment } from '../image/dto/comment';
import { CommentRequest } from './dto/comment-request';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public getComments(id: String, page: number = 0): Observable<Page<Comment>> {
    return this.http.get<Page<Comment>>(`${this.apiServerUrl}/image/${id}/comments?page=${page}`, { headers: this.getHeaders() });
  }

  public deleteComment(id: number): Observable<any> {
    return this.http.delete(`${this.apiServerUrl}/comment/${id}`, { headers: this.getHeaders() });
  }

  public addComment(id: String, request: CommentRequest): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiServerUrl}/image/${id}/comment`, request, { headers: this.getHeaders() });
  }
}
