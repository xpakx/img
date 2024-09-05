import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SearchResult } from './search/dto/search-result';
import { SearchService } from './search/search.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Page } from './gallery/dto/page';
import { debounceTime, distinctUntilChanged, fromEvent, switchMap } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'img';
  @ViewChild('searchInput') searchInput!: ElementRef<HTMLInputElement>;
  searchResults: SearchResult[] = [];

  constructor(private router: Router, private searchService: SearchService) { }

  get logged(): boolean {
    return localStorage.getItem("username") != null;
  }

  search(): void {
    const value = this.searchInput.nativeElement.value;
    if (value) {
      this.router.navigate(['/search'], {queryParams: {query: value}});
    }
  }

  ngAfterViewInit() {
    fromEvent(this.searchInput.nativeElement, 'input')
    .pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(() => {
        const value = this.searchInput.nativeElement.value;
        if (value) {
          return this.searchService.searchProfile(value, 0, true);
        } else {
          return [];
        }
      })
    )
    .subscribe({
      next: (results: Page<SearchResult>) => (this.searchResults = results.content),
        error: (err: HttpErrorResponse) => console.log(err),
    });
  }
}
