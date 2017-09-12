import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Location }                 from '@angular/common';

import { AntennaService } from './antenna.service';
import 'rxjs/add/operator/switchMap';

@Component( {

    selector: 'antenna-detail',

    templateUrl: 'component/antenna-detail.component.html',

    constructor(
        private antennaService: AntennaService,
        private route: ActivatedRoute,
        private location: Location
    ) {}
} )

export class AntennaDetailComponent implements OnInit{

    @Input() antenna: Antenna;


    ngOnInit(): void {
        this.route.paramMap
            .switchMap((params: ParamMap) => this.antennaService.getAntenna(+params.get('id')))
            .subscribe(antenna => this.antenna = antenna);
    }


    goBack(): void {
        this.location.back();
    }
}