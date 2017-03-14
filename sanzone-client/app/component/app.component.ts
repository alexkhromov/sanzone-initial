/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from 'angular2/core';
import {TwittComponent} from "./twitt.component";

@Component({

    selector: 'my-app',

    template: `<h1>My First Angular 2 App</h1>
               <twitt></twitt>`,

    directives: [ TwittComponent ]
})

export class AppComponent { }