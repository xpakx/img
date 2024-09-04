import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Page } from 'src/app/gallery/dto/page';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
  @Output("open") pageEvent = new EventEmitter<number>();
  pages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;

  constructor() { }

  @Input("page") set pageObject(page: Page<any>) {
    this.pages = page.totalPages;
    this.page = page.number;
    this.last = page.last;
    this.first = page.first;
  }

  ngOnInit(): void {

  }

  open(page: number): void {
    this.pageEvent.emit(page);
  }
}
