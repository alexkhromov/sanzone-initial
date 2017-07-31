/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';

@Component( {

    selector: 'demo',

    template: `<div class="container">
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
`
} )

export class DemoComponent {}