import { Component, OnInit } from '@angular/core';
import { SearchResult } from '../dto/search-result';
import { SearchService } from '../search.service';
import { ActivatedRoute } from '@angular/router';
import { Page } from 'src/app/gallery/dto/page';
import { HttpErrorResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {
  profiles: SearchResult[] = [];
  apiUrl: String = environment.apiUrl;

  constructor(private searchService: SearchService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if(!params['query']) {
        //TODO: error
      }
      this.searchService.searchProfile(params['query']).subscribe({
        next: (result: Page<SearchResult>) => this.profiles = result.content,
        error: (err: HttpErrorResponse) => console.log(err),
      });
    });
  }

}
