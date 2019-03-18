import { Component, OnInit } from '@angular/core';

import { Antenna } from '../model/antenna';
import { AntennaService } from '../service/antenna.service';


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