/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}      from '@angular/core';
import {CommonModule}      from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from "./app.component";
import {NavbarComponent} from "./navbar.component";
import {ExampleComponent} from "./example.component";
import {HeroComponent} from "./hero.component";

@NgModule( {

    imports: [ CommonModule, BrowserModule ],

    declarations: [

        AppComponent,
        NavbarComponent,
        ExampleComponent,
        HeroComponent
    ],


    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }