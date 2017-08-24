import { Component, OnInit } from '@angular/core';
import { Antenna } from './antenna';
import { AntennaService } from './antenna.service';


@Component({
    selector: 'my-dashboard',
    template: ` <h3>Активные антенны</h3>
                <div *ngFor="let antenna of antennas">
                    <div>
                            <h4>{{antenna.name}}</h4>
                    </div>
                </div>`,
})

export class DashboardComponent implements OnInit {

    antennas: Antenna[] = [];

    constructor(private antennaService: AntennaService) { }

    ngOnInit(): void {
        this.antennaService.getAntennas()
            .then(antennas => this.antennas = antennas.slice(0, 10));
    }
}