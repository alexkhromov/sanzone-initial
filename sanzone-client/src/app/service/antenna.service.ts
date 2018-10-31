import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Antenna } from '../model/antenna';
import { MessageService } from './message.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({ providedIn: 'root' })
export class AntennaService {

  private antennasUrl = 'http://localhost:8081/v1/test';  // URL to web api

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  /** GET antennas from the server */
  getAntennas (): Observable<Antenna[]> {
    return this.http.get<Antenna[]>(this.antennasUrl)
      .pipe(
        tap(antennas => this.log('fetched antennas')),
        catchError(this.handleError('getAntennas', []))
      );
  }
  getConfig() {
    return this.http.get('http://localhost:8081/v1/test');
  }
  /** GET antenna by id. Return `undefined` when id not found */
  getAntennaNo404<Data>(id: number): Observable<Antenna> {
    const url = `${this.antennasUrl}/?id=${id}`;
    return this.http.get<Antenna[]>(url)
      .pipe(
        map(antennas => antennas[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          this.log(`${outcome} antenna id=${id}`);
        }),
        catchError(this.handleError<Antenna>(`getAntenna id=${id}`))
      );
  }

  /** GET antenna by id. Will 404 if id not found */
  getAntenna(id: number): Observable<Antenna> {
    const url = `${this.antennasUrl}/${id}`;
    return this.http.get<Antenna>(url).pipe(
      tap(_ => this.log(`fetched antenna id=${id}`)),
      catchError(this.handleError<Antenna>(`getAntenna id=${id}`))
    );
  }

  /* GET antennas whose name contains search term */
  searchAntennas(term: string): Observable<Antenna[]> {
    if (!term.trim()) {
      // if not search term, return empty antenna array.
      return of([]);
    }
    return this.http.get<Antenna[]>(`${this.antennasUrl}/?name=${term}`).pipe(
      tap(_ => this.log(`found antennas matching "${term}"`)),
      catchError(this.handleError<Antenna[]>('searchAntennas', []))
    );
  }

  //////// Save methods //////////

  /** POST: add a new antenna to the server */
  addAntenna (antenna: Antenna): Observable<Antenna> {
    return this.http.post<Antenna>(this.antennasUrl, antenna, httpOptions).pipe(
      tap((antenna: Antenna) => this.log(`added antenna w/ id=${antenna.id}`)),
      catchError(this.handleError<Antenna>('addAntenna'))
    );
  }

  /** DELETE: delete the antenna from the server */
  deleteAntenna (antenna: Antenna | number): Observable<Antenna> {
    const id = typeof antenna === 'number' ? antenna : antenna.id;
    const url = `${this.antennasUrl}/${id}`;

    return this.http.delete<Antenna>(url, httpOptions).pipe(
      tap(_ => this.log(`deleted antenna id=${id}`)),
      catchError(this.handleError<Antenna>('deleteAntenna'))
    );
  }

  /** PUT: update the antenna on the server */
  updateAntenna (antenna: Antenna): Observable<any> {
    return this.http.put(this.antennasUrl, antenna, httpOptions).pipe(
      tap(_ => this.log(`updated antenna id=${antenna.id}`)),
      catchError(this.handleError<any>('updateAntenna'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a AntennaService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`AntennaService: ${message}`);
  }
}
