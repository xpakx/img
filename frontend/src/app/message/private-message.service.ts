import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Page } from '../gallery/dto/page';
import { PrivateMessageRequest } from './dto/private-message-request';
import { PrivateMessage } from './dto/private-message';

@Injectable({
  providedIn: 'root'
})
export class PrivateMessageService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public sendMessage(request: PrivateMessageRequest): Observable<PrivateMessage> {
    return this.http.post<PrivateMessage>(`${this.apiServerUrl}/messages`, request, { headers: this.getHeaders() });
  }

  public deleteMessage(id: number): Observable<any> {
    return this.http.delete(`${this.apiServerUrl}/messages/${id}`, { headers: this.getHeaders() });
  }

  public getMessages(page: number = 0): Observable<Page<PrivateMessage>> {
    return this.http.get<Page<PrivateMessage>>(`${this.apiServerUrl}/messages?page=${page}`, { headers: this.getHeaders() });
  }

  public getSentMessages(page: number = 0): Observable<Page<PrivateMessage>> {
    return this.http.get<Page<PrivateMessage>>(`${this.apiServerUrl}/messages/sent?page=${page}`, { headers: this.getHeaders() });
  }

  public getUnreadMessages(page: number = 0): Observable<Page<PrivateMessage>> {
    return this.http.get<Page<PrivateMessage>>(`${this.apiServerUrl}/messages/unread?page=${page}`, { headers: this.getHeaders() });
  }
}
