/**
 * Created by DEV on 13.03.2017.
 */
import { Component, OnInit } from '@angular/core';
import { Antenna } from './antenna';
import { AntennaService } from './antenna.service';


@Component( {

    selector: 'my-antennas',

    template: `
                <div class="container">
                    <a routerLink="/dashboard">Dashboard</a>
                    <a routerLink="/antennas">Антенны</a>
                    <h1>{{title}}</h1>
            
                    <ul class="antennas">
                        <li *ngFor="let antenna of antennas"
                            [class.selected]="antenna === selectedAntenna"
                            (click)="onSelect(antenna)">
                        <span class="badge">{{antenna.id}}</span> {{antenna.name}}
                        </li>
                    </ul>
                    
                    <antenna-detail [antenna]="selectedAntenna"></antenna-detail>
                    
                </div>

`,
    styles: [`  `],
    providers: [AntennaService]
} )

export class AntennasComponent implements OnInit {
    title = 'Список антенн';
    antennas: Antenna[];
    selectedAntenna: Antenna;

    constructor(private antennaService: AntennaService) { }

    getAntennas(): void {
        this.antennaService.getAntennas().then(antennas => this.antennas = antennas);
    }

    ngOnInit(): void {
        this.getAntennas();
    }

    onSelect(antenna: Antenna): void {
        this.selectedAntenna = antenna;
    }
}

