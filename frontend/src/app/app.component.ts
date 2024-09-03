import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SearchResult } from './search/dto/search-result';
import { SearchService } from './search/search.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Page } from './gallery/dto/page';

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

  onInputChange(): void { // TODO
    console.log("hm");
    const value = this.searchInput.nativeElement.value;
    if (value) {
      this.searchService.searchProfile(value).subscribe({
        next: (results: Page<SearchResult>) => this.searchResults = results.content,
        error: (err: HttpErrorResponse) => console.log(err),
      });
    }
  }
}
