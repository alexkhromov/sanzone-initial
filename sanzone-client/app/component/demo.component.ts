/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';
import { Hero } from './antenna';

const HEROES: Hero[] = [
    { id: 11, name: 'Mr. Nice' },
    { id: 12, name: 'Narco' },
    { id: 13, name: 'Bombasto' },
    { id: 14, name: 'Celeritas' },
    { id: 15, name: 'Magneta' },
    { id: 16, name: 'RubberMan' },
    { id: 17, name: 'Dynama' },
    { id: 18, name: 'Dr IQ' },
    { id: 19, name: 'Magma' },
    { id: 20, name: 'Tornado' }
];

@Component( {

    selector: 'demo',

    template: `
<div class="container">

 <h1>{{title}}</h1>
    <h2>My Heroes</h2>
    <ul class="heroes">
      <li *ngFor="let hero of heroes"
        [class.selected]="hero === selectedHero"
        (click)="onSelect(hero)">
        <span class="badge">{{hero.id}}</span> {{hero.name}}
      </li>
    </ul>
            <antenna-detail [hero]="selectedHero"></antenna-detail>
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
  `]
} )

export class DemoComponent {
    title = 'Tour of Heroes';
    heroes = HEROES;
    selectedHero: Hero;

    onSelect(hero: Hero): void {
        this.selectedHero = hero;
    }
}

}