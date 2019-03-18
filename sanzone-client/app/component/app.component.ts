/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';

@Component( {

    selector: 'my-app',

    template: `<navbarComponent></navbarComponent>
               <router-outlet></router-outlet>
              `
          } )

export class AppComponent { }