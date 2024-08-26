import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private apiServerUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    let token = localStorage.getItem("token");
    return new HttpHeaders({'Authorization' : `Bearer ${token}`});
  }

  public sendImage(files: FileList): Observable<any> {
    let formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }
    return this.http.post(`${this.apiServerUrl}/image`, formData, { headers: this.getHeaders() });
  }

  public sendAvatar(files: FileList): Observable<any> {
    let formData: FormData = new FormData();
    formData.append('files', files[0]);
    return this.http.post(`${this.apiServerUrl}/avatar`, formData, { headers: this.getHeaders() });
  }
}

