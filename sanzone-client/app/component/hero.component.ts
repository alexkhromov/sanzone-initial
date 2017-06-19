import { Component } from '@angular/core';

export class Hero {
    id: number;
    name: string;
    fullImagePath: string;

}

const HEROES: Hero[] = [
    { id: 1, name: 'Расчет в горизонтальной плоскости', fullImagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/d4bb0131-0bea-4f42-8e66-1bf3d14ff665_sanzone_H_hbeem2.jpg' },
    { id: 2, name: 'Расчет в вертикальной плоскости', fullImagePath: 'http://res.cloudinary.com/emfs/image/upload/v1497878339/f5bb36c9-1213-4259-b3de-56776c47a7a2_sanzone_V_ppm2zh.jpg' },
];

@Component({
    selector: 'heroComponent',
    template: `
    <h1>{{title}}</h1>
    <h2>My Heroes</h2>
    <ul class="heroes">
      <li *ngFor="let hero of heroes">
        <span class="badge">{{hero.id}}</span> {{hero.name} 
        <a href="#">
   <img class="img-responsive" alt="Responsive image" src="{{fullImagePath.name}}">
   </a>
      </li>
    </ul>

  `,
    styleUrls: ['../component/example.component.css']
})
export class HeroComponent {
    title = 'Tour of Heroes';
    heroes = HEROES;
    selectedHero: Hero;
    }
}
