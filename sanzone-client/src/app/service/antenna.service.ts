import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs';
import { Antenna } from '../model/antenna';


@Injectable()
export class AntennaService {

    private headers = new Headers({ 'Content-Type': 'application/json' });
    private antennasUrl = 'api/antennas';

    constructor(private http: Http) { }

    getAntennas(): Promise<Antenna[]> {
        return this.http.get(this.antennasUrl)
            .toPromise()
            .then(response => response.json().data as Antenna[])
            .catch(this.handleError);
    }


    getAntenna(id: number): Promise<Antenna> {
        const url = `${this.antennasUrl}/${id}`;
        return this.http.get(url)
            .toPromise()
            .then(response => response.json().data as Antenna)
            .catch(this.handleError);
    }

    delete(id: number): Promise<void> {
        const url = `${this.antennasUrl}/${id}`;
        return this.http.delete(url, { headers: this.headers })
            .toPromise()
            .then(() => null)
            .catch(this.handleError);
    }

    create(name: string): Promise<Antenna> {
        return this.http
            .post(this.antennasUrl, JSON.stringify({ name: name }), { headers: this.headers })
            .toPromise()
            .then(res => res.json().data as Antenna)
            .catch(this.handleError);
    }

    update(antenna: Antenna): Promise<Antenna> {
        const url = `${this.antennasUrl}/${antenna.id}`;
        return this.http
            .put(url, JSON.stringify(antenna), { headers: this.headers })
            .toPromise()
            .then(() => antenna)
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

}