import { InMemoryDbService } from 'angular-in-memory-web-api';
export class InMemoryDataService implements InMemoryDbService {
    createDb() {

        const antennas = [
            { id: 0, name: 'Kathrein 841840', sector: 'A', latitude: 54, longitude: 23, azimuth: 3, downTilt: 1, height: 15, power: 20, gain: 3, tractLost: 3, earthFactor: 1, diagramWidthHalfPowerH: 60,  diagramWidthHalfPowerV: 15, operator: 'MTS', range: 'GSM'},
            { id: 1, name: 'Kathrein 841842', sector: 'B', latitude: 54, longitude: 23, azimuth: 3, downTilt: 1, height: 15, power: 20, gain: 3, tractLost: 3, earthFactor: 1, diagramWidthHalfPowerH: 60,  diagramWidthHalfPowerV: 15, operator: 'MTS', range: 'GSM'},
            { id: 2, name: 'Kathrein 841843', sector: 'C', latitude: 54, longitude: 23, azimuth: 3, downTilt: 1, height: 15, power: 20, gain: 3, tractLost: 3, earthFactor: 1, diagramWidthHalfPowerH: 60,  diagramWidthHalfPowerV: 15, operator: 'MTS', range: 'GSM'},
            { id: 0, name: 'Kathrein 841840', sector: 'A', latitude: 54, longitude: 23, azimuth: 3, downTilt: 1, height: 15, power: 20, gain: 3, tractLost: 3, earthFactor: 1, diagramWidthHalfPowerH: 60,  diagramWidthHalfPowerV: 15, operator: 'velcom', range: 'GSM'},
            { id: 1, name: 'Kathrein 841842', sector: 'B', latitude: 54, longitude: 23, azimuth: 3, downTilt: 1, height: 15, power: 20, gain: 3, tractLost: 3, earthFactor: 1, diagramWidthHalfPowerH: 60,  diagramWidthHalfPowerV: 15, operator: 'MTS', range: 'GSM'},
            { id: 2, name: 'Kathrein 841843', sector: 'C', latitude: 54, longitude: 23, azimuth: 3, downTilt: 1, height: 15, power: 20, gain: 3, tractLost: 3, earthFactor: 1, diagramWidthHalfPowerH: 60,  diagramWidthHalfPowerV: 15, operator: 'MTS', range: 'GSM'},
        ];
        return {antennas};
    }
}