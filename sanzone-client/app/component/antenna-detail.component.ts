import { Component, Input } from '@angular/core';
import { Hero } from './antenna';
@Component({
    selector: 'antenna-detail',
    template: `
    <div *ngIf="antenna">
      <h2>{{antenna.name}} details!</h2>
      <div><label>id: </label>{{antenna.id}}</div>
      <div>
        <label>name: </label>
        <input [(ngModel)]="antenna.name" placeholder="name"/>
      </div>
    </div>
  `
})
export class AntennaDetailComponent {
    @Input() antenna: Antenna;
}