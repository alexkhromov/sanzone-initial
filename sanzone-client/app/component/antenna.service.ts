import { Injectable } from '@angular/core';

import { Antenna } from './antenna';
import { ANTENNAS } from './mock-antenna';

@Injectable()
    export class AntennaService {
        getAntennas(): Promise<Antenna[]> {
            return Promise.resolve(ANTENNAS);
            }
    }