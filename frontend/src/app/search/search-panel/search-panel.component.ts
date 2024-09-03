import { Component, Input } from '@angular/core';
import { SearchResult } from '../dto/search-result';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-search-panel',
  templateUrl: './search-panel.component.html',
  styleUrl: './search-panel.component.css'
})
export class SearchPanelComponent {
  apiUrl: String = environment.apiUrl;
  @Input("results") results: SearchResult[] = [];
}
