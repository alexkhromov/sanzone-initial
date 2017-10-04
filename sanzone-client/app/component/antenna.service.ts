import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Antenna } from './antenna';


@Injectable()
    export class AntennaService {

    private antennasUrl = 'api/antennas';

    constructor(private http: Http) { }

        getAntennas(): Promise<Antenna[]> {
            return this.http.get(this.antennasUrl)
                .toPromise()
                .then(response => response.json().data as Antenna[])
                .catch(this.handleError);
            }


    getAntenna(id: number): Promise<Antenna> {
        return this.getAntennas()
            .then(antennas => antennas.find(antenna => antenna.id === id));
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

    }