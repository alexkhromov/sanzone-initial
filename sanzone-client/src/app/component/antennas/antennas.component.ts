import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Antenna } from '../../model/antenna';
import { AntennaService } from '../../service/antenna.service';


@Component({
    selector: 'my-antennas',
    templateUrl: './antennas.component.html',
    styleUrls: ['./antennas.component.css']
})

export class AntennasComponent implements OnInit {
    title = 'Параметры антенн';
    antennas: Antenna[];
    selectedAntenna: Antenna;

    constructor(private antennaService: AntennaService,
        private router: Router) { }

    getAntennas(): void {
        this.antennaService
            .getAntennas()
            .then(antennas => this.antennas = antennas);
    }

    add(name: string): void {
        name = name.trim();
        if (!name) { return; }
        this.antennaService.create(name)
            .then(antenna => {
                this.antennas.push(antenna);
                this.selectedAntenna = null;
            });
    }

    delete(antenna: Antenna): void {
        this.antennaService
            .delete(antenna.id)
            .then(() => {
                this.antennas = this.antennas.filter(h => h !== antenna);
                if (this.selectedAntenna === antenna) { this.selectedAntenna = null; }
            });
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

