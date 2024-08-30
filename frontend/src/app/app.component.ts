import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'img';
  @ViewChild('searchInput') searchInput!: ElementRef<HTMLInputElement>;

  constructor(private router: Router) { }

  get logged(): boolean {
    return localStorage.getItem("username") != null;
  }

  search(): void {
    const value = this.searchInput.nativeElement.value;
    if (value) {
      this.router.navigate(['/search'], {queryParams: {query: value}});
    }
  }
}
