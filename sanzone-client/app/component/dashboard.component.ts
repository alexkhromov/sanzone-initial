import { Component, OnInit } from '@angular/core';
import { Antenna } from './antenna';
import { AntennaService } from './antenna.service';


@Component({
    selector: 'my-dashboard',
    templateUrl: 'component/dashboard.component.html',
})

export class DashboardComponent implements OnInit {

    antennas: Antenna[] = [];

    constructor(private antennaService: AntennaService) { }

    ngOnInit(): void {
        this.antennaService.getAntennas()
            .then(antennas => this.antennas = antennas.slice(0, 10));
    }
}