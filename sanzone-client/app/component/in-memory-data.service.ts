import { InMemoryDbService } from 'angular-in-memory-web-api';
export class InMemoryDataService implements InMemoryDbService {
    createDb() {

        const antennas = [
            { id: 0, name: 'Antenna 0', latitude:54, longitude: 23},
            { id: 1, name: 'Antenna 1',latitude:54, longitude: 23 },
            { id: 2, name: 'Antenna 2', latitude:54, longitude: 23 },
            { id: 3, name: 'Antenna 3', latitude:54, longitude: 23 },
            { id: 4, name: 'Antenna 4', latitude:54, longitude: 23 },
            { id: 5, name: 'Antenna 5', latitude:54, longitude: 23 },
            { id: 6, name: 'Antenna 6', latitude:54, longitude: 23 },
            { id: 7, name: 'Antenna 7', latitude:54, longitude: 23 },
            { id: 8, name: 'Antenna 8', latitude:54, longitude: 23 },
            { id: 9, name: 'Antenna 9', latitude:54, longitude: 23 },
            { id: 10, name: 'Antenna 10', latitude:54, longitude: 23}
        ];
        return {antennas};
    }
}