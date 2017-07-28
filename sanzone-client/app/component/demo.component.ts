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
      <th>Тип антенны</th>
      <th>Мощность несущей, Вт.</th>
      <th>Кол-во каналов (несущих)</th>
      <th>Высота подвеса, м.</th>
      <th>Собственник</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">1</th>
      <td> <input [(ngModel)]="type" placeholder="Тип"></td>
      <td> <input [(ngModel)]="name" placeholder="Мощность"></td>
      <td>@mdo</td>
      <td>@mdo</td>
    </tr>
    <tr>
      <th scope="row">2</th>
      <td>Jacob</td>
      <td>Thornton</td>
      <td>@fat</td>
    </tr>
    <tr>
      <th scope="row">3</th>
      <td>Larry</td>
      <td>the Bird</td>
      <td>@twitter</td>
    </tr>
  </tbody>
</table>
</div>


               <div class="panel panel-primary">
                 <div class="panel-body">
    Basic panel example
  </div>
              <table class="table table-bordered">
              <input [(ngModel)]="name" placeholder="name">
              </table> 
              </div>
              
              <div class="row">
  <div class="col-xs-2">
    <input type="text" class="form-control" placeholder=".col-xs-2">
  </div>
  <div class="col-xs-3">
    <input type="text" class="form-control" placeholder=".col-xs-3">
  </div>
  <div class="col-xs-4">
    <input type="text" class="form-control" placeholder=".col-xs-4">
  </div>
</div>

              </div>
`
} )

export class DemoComponent {}