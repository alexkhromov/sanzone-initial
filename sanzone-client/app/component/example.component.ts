import { Component } from '@angular/core';

export class Hero {
    id: number;
    name: string;
    article: string;
    imagePath: string;
}

const HEROES: Hero[] = [
    { id: 1, name: 'Расчет санитарно-защитной зоны в горизонтальной плоскости антенн в одной точке' article:'Данный расчет позволяет рассчитывать санитарно-защитнйю зону при расположении антенн в одной точке горизонтальной плссокти,т.е. при совпадении широты и долготы', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/d4bb0131-0bea-4f42-8e66-1bf3d14ff665_sanzone_H_hbeem2.jpg' },
    { id: 2, name: 'Расчет в горизонтальной плоскости 2',article:'', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/df2bce65-c2be-4a17-9d03-9beb5bda1fac_sanzone_H_gizvin.jpg' },
    { id: 3, name: 'Расчет в горизонтальной плоскости 3', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/ca898906-3643-4cc2-9d68-43c7db7194a7_sanzone_H_cqeedc.jpg' },
    { id: 4, name: 'Расчет в горизонтальной плоскости 4', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/4a231c7a-00ec-46bf-a9d8-3c7db3c50348_sanzone_H_cj4xld.jpg' },
    { id: 5, name: 'Расчет в горизонтальной плоскости 5', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/8145a307-f9dc-4528-a8f9-e4df79c0fdab_sanzone_H_ugadio.jpg' },
    { id: 6, name: 'Расчет в горизонтальной плоскости 6', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/1c534d8a-da32-4ce8-b466-aed1622b3776_sanzone_H_t9wlxz.jpg' },
    { id: 7, name: 'Расчет в вертикальной плоскости', imagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/f5bb36c9-1213-4259-b3de-56776c47a7a2_sanzone_V_ppm2zh.jpg' },
];

@Component({
    selector: 'exampleComponent',
    template: `
 <div class="container marketing">
    <ul class="heroes">
      <li *ngFor="let hero of heroes">
      <hr class="featurette-divider">
          <div class="row featurette">
        <div class="col-md-7">
          <h3>Решение №{{hero.id}}
          <p><span class="text-muted">{{hero.name}}</span></p></h3>
          <p class="lead">{{hero.article}}</p>
        </div>
        <div class="col-md-5">
          <img class="featurette-image img-responsive center-block" src={{hero.imagePath}} alt="Generic placeholder image">
        </div>
      </div>
      </li>
    </ul>
    <hr class="featurette-divider">
 </div>
  `,
    styleUrls: ['']
})
export class ExampleComponent {
    heroes = HEROES;
     }
}
