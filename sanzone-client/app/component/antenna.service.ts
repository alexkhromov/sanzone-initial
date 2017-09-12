import { Injectable } from '@angular/core';

import { Antenna } from './antenna';
import { ANTENNAS } from './mock-antenna';

@Injectable()
    export class AntennaService {
        getAntennas(): Promise<Antenna[]> {
            return Promise.resolve(ANTENNAS);
            }


    getAntenna(id: number): Promise<Antenna> {
        return this.getAntennas()
            .then(antennas => antennas.find(antenna => antenna.id === id));
    }
    }