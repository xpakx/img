import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ImageDetails } from '../gallery/dto/image-details';
import { Image } from '../gallery/dto/image';
import { UpdateImageRequest } from './dto/update-image-request';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public getImageDetails(id: String): Observable<ImageDetails> {
    return this.http.get<ImageDetails>(`${this.apiServerUrl}/image/${id}/details`, { headers: this.getHeaders() });
  }

  public deleteImage(id: String): Observable<any> {
    return this.http.delete(`${this.apiServerUrl}/image/${id}`, { headers: this.getHeaders() });
  }

  public editImage(id: String, request: UpdateImageRequest): Observable<Image> {
    return this.http.put<Image>(`${this.apiServerUrl}/image/${id}/data`, request, { headers: this.getHeaders() });
  }
}
