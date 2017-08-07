import { Component } from '@angular/core';

@Component({
    selector: 'my-demo',
    template: `
   <h1>{{title}}</h1>
   <a routerLink="/antennas">Antennas</a>
   <router-outlet></router-outlet>
  `
})
export class DemoComponent {
    title = 'Tour of Heroes';
}