import {  Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location }                 from '@angular/common';

import { Antenna } from '../../model/antenna';
import { AntennaService } from '../../service/antenna.service';


@Component( {
    selector: 'antenna-detail',
    templateUrl: './antenna-detail.component.html',
    styleUrls: [ './antenna-detail.component.css' ]

} )

export class AntennaDetailComponent implements OnInit{

  @Input() antenna: Antenna;

    constructor(
        private antennaService: AntennaService,
        private route: ActivatedRoute,
        private location: Location
    ) {}


    ngOnInit(): void {
     // this.getAntenna();
    }

  // getAntenna(): void {
  //   const id = +this.route.snapshot.paramMap.get('id');
  //   this.antennaService.getConfig(id)
  //     .subscribe(antenna => this.antenna = antenna);
  // }

  goBack(): void {
    this.location.back();
  }

  // save(): void {
  //   this.antennaService.updateAntenna(this.antenna)
  //     .subscribe(() => this.goBack());
  // }
}
