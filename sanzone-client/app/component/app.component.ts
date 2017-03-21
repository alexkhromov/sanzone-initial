/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from 'angular2/core';
import {TwittComponent} from "./twitt.component";
import {NavbarComponent} from "./navbar/navbar.component";

@Component({

    selector: 'my-app',

    template: `<navbar></navbar>
               <h1>My First Angular 2 App</h1>
                                     
               
               <twitt></twitt>`,

    directives: [ NavbarComponent, TwittComponent ]
})

export class AppComponent { }