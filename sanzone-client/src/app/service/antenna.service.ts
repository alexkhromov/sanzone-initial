import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Antenna } from '../model/antenna';

@Injectable({
  providedIn: 'root'
})
export class AntennaService {

  configUrl = 'http://localhost:8081/v1/test';

  constructor(private http: HttpClient) { }

  getConfig() {
    // now returns an Observable of Config
    return this.http.get<Antenna>(this.configUrl);
  }


  getConfigResponse(): Observable<HttpResponse<Antenna>> {
    return this.http.get<Antenna>(
      this.configUrl, { observe: 'response' });
  }

}
