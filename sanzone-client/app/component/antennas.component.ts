/**
 * Created by DEV on 13.03.2017.
 */
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Antenna } from './antenna';
import { AntennaService } from './antenna.service';


@Component( {

    selector: 'my-antennas',
    templateUrl: 'component/antennas.component.html',
    styles: [`  `]
    } )

export class AntennasComponent implements OnInit {
    title = 'Список антенн';
    antennas: Antenna[];
    selectedAntenna: Antenna;

    constructor(private antennaService: AntennaService,
                private router: Router) { }

    getAntennas(): void {
        this.antennaService.getAntennas().then(antennas => this.antennas = antennas);
    }

    ngOnInit(): void {
        this.getAntennas();
    }

    onSelect(antenna: Antenna): void {
        this.selectedAntenna = antenna;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedAntenna.id]);
    }
}

