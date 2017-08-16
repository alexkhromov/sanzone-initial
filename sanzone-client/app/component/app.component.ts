/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';

@Component( {

    selector: 'my-app',

    template: `<navbarComponent></navbarComponent>
   <!--<h1>{{title}}</h1>
   <a routerLink="/main">Главная</a>
   <a routerLink="/demo">Деморасчет</a>
   <a routerLink="/antennas">Антенны</a>-->
   <router-outlet></router-outlet>
               <!--<my-demo></my-demo>-->
               <twit></twit>

              `
            } )

export class AppComponent { }