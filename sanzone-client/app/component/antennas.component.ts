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

 <h1>{{title}}</h1>
    <h2>My Heroes</h2>
    <ul class="antennas">
      <li *ngFor="let antenna of antennas"
        [class.selected]="antenna === selectedAntenna"
        (click)="onSelect(antenna)">
        <span class="badge">{{antenna.id}}</span> {{antenna.name}}
      </li>
    </ul>
            <antenna-detail [antenna]="selectedAntenna"></antenna-detail>
        </div>



<div class="container">
<div class="panel panel-primary">
  <!-- Default panel contents -->
  <div class="panel-heading">Введите параметры радиопередающих устройств</div>

  <!-- Table -->
<table class="table">
  <thead>
    <tr>
      <th>#</th>
              <th>Latitude</th>
              <th >Longitude</th>
              <th>Azimuth</th>
              <th>Down Tilt</th>
              <th>Height</th>
              <th>Power</th>
              <th>Gain</th>
              <th>Tract Lost</th>
              <th>Earth Factor</th>
              <th>Diagram WidthHalf PowerH</th>
              <th>Diagram WidthHalf PowerV</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">1</th>
      <th><input size="5" [(ngModel)]="Latitude" placeholder="Latitude"></th>
      <th><input size="5" [(ngModel)]="Longitude" placeholder="Longitude"></th>
      <th><input size="5" [(ngModel)]="Azimuth" placeholder="Azimuth"></th>
      <th><input size="5" [(ngModel)]="DownTilt" placeholder="DownTilt"></th>
      <th><input size="5" [(ngModel)]="Height" placeholder="Height"></th>
      <th><input size="5" [(ngModel)]="Power" placeholder="Power"></th>
      <th><input size="5" [(ngModel)]="Gain" placeholder="Gain"></th>
      <th><input size="5" [(ngModel)]="TractLost" placeholder="TractLost"></th>
      <th><input size="5" [(ngModel)]="EarthFactor" placeholder="EarthFactor"></th>
      <th><input size="5" [(ngModel)]="DiagramWidthHalfPowerH" placeholder="DiagramWidthHalfPowerH"></th>
      <th><input size="5" [(ngModel)]="DiagramWidthHalfPowerV" placeholder="DiagramWidthHalfPowerV"></th> 
    </tr>
  </tbody>
</table>
</div>

              </div>
`,
    styles: [`
    .selected {
      background-color: #CFD8DC !important;
      color: white;
    }
    .heroes {
      margin: 0 0 2em 0;
      list-style-type: none;
      padding: 0;
      width: 15em;
    }
    .heroes li {
      cursor: pointer;
      position: relative;
      left: 0;
      background-color: #EEE;
      margin: .5em;
      padding: .3em 0;
      height: 1.6em;
      border-radius: 4px;
    }
    .heroes li.selected:hover {
      background-color: #BBD8DC !important;
      color: white;
    }
    .heroes li:hover {
      color: #607D8B;
      background-color: #DDD;
      left: .1em;
    }
    .heroes .text {
      position: relative;
      top: -3px;
    }
    .heroes .badge {
      display: inline-block;
      font-size: small;
      color: white;
      padding: 0.8em 0.7em 0 0.7em;
      background-color: #607D8B;
      line-height: 1em;
      position: relative;
      left: -1px;
      top: -4px;
      height: 1.8em;
      margin-right: .8em;
      border-radius: 4px 0 0 4px;
    }
  `],
    providers: [AntennaService]
} )

export class AntennasComponent implements OnInit {
    title = 'Tour of Heroes';
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

