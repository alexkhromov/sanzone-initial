/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}      from '@angular/core';
import {CommonModule}      from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from "./app.component";
import {NavbarComponent} from "./navbar.component";
import {ExampleComponent} from "./example.component";
import {PostComponent} from "./arhiv/post.component";
import {TwitComponent} from "./arhiv/twit.component";
import {HeartComponent} from "./arhiv/heart.component";
import {PostService} from "../service/post.service";


@NgModule( {

    imports: [ CommonModule, BrowserModule ],

    declarations: [

        AppComponent,
        NavbarComponent,
        ExampleComponent,
        TwitComponent,
        PostComponent,
        HeartComponent
    ],

    providers: [
        PostService
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }