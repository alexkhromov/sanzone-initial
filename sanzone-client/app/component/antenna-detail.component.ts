import 'rxjs/add/operator/switchMap';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Location }                 from '@angular/common';

import { Antenna } from '../model/antenna';
import { AntennaService } from '../service/antenna.service';


@Component( {

    selector: 'antenna-detail',

    templateUrl: 'component/antenna-detail.component.html',


} )

export class AntennaDetailComponent implements OnInit{

    antenna: Antenna;

    constructor(
        private antennaService: AntennaService,
        private route: ActivatedRoute,
        private location: Location
    ) {}


    ngOnInit(): void {
        this.route.paramMap
            .switchMap((params: ParamMap) => this.antennaService.getAntenna(+params.get('id')))
            .subscribe(antenna => this.antenna = antenna);
    }


    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.antennaService.update(this.antenna)
            .then(() => this.goBack());
    }
}