import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Antenna } from '../model/antenna';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    //'Authorization': 'my-auth-token'
  })
};

@Injectable({
  providedIn: 'root'
})
export class AntennaService {

  configUrl = 'http://localhost:8081/v1/test';
  configUrl2 = 'http://localhost:8081/v1/summary';
  constructor(private http: HttpClient) { }

  getConfig() {
    // now returns an Observable of Config
    return this.http.get<Antenna>(this.configUrl);
  }


  getConfigResponse(): Observable<HttpResponse<Antenna>> {
    return this.http.get<Antenna>(
      this.configUrl, { observe: 'response' })
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };


  /** POST: add a new antenna to the database */
addAntenna (antenna: any): Observable<Antenna> {
  return this.http.post<Antenna>(this.configUrl2, antenna, httpOptions)
    .pipe(
      catchError(this.handleError)//('addAntenna', antenna))
    );
}
}
