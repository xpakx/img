import { Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { Observable, of } from 'rxjs';
import { LogEvent } from './dto/event';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LogService {
  apiUrl: String = environment.apiUrl;

  constructor(private httpClient: HttpClient) { }

  getEvents(): Observable<LogEvent[]> {
    return of([
      {id: "1111"},
      {id: "aaaa"},
    ]);
  }

  getEventsReal(): Observable<LogEvent[]> {
    return this.httpClient.get<LogEvent[]>(this.apiUrl + "/events");
  }
}
