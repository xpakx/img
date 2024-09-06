import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LogService } from './log.service';
import { LogEvent } from './dto/event';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Image Logs';
  events: LogEvent[] = [];

  constructor(private logService: LogService) { }

  ngOnInit(): void {
      this.logService.getEvents().subscribe({
        next: (result: LogEvent[]) => this.events = result,
        error: (err: HttpErrorResponse) => console.log(err),
      });
  }
}
