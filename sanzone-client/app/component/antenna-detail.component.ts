import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Location }                 from '@angular/common';

import { AntennaService } from './antenna.service';
import 'rxjs/add/operator/switchMap';

@Component( {

    selector: 'antenna-detail',

    template:
        `<div *ngIf="antenna">
            
            <h2>{{antenna.name}} details!</h2>
            
            <div>
                <label>id: </label>{{antenna.id}}
            </div>
            
            <div>
                <label>name: </label>
                <input [(ngModel)]="antenna.name" placeholder="name"/>
                <label>latitude: </label>
                <input [(ngModel)]="antenna.latitude" placeholder="latitude"/>
            </div>
        </div>
`,
    constructor(
        private antennaService: AntennaService,
        private route: ActivatedRoute,
        private location: Location
    ) {}
} )

export class AntennaDetailComponent {

    @Input() antenna: Antenna;
}