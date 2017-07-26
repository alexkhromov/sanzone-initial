/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}      from '@angular/core';
import {CommonModule}      from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from "./app.component";
import {NavbarComponent} from "./navbar.component";
import {PostComponent} from "./post.component";
import {TwitComponent} from "./twit.component";
import {PostService} from "../service/post.service";


@NgModule( {

    imports: [ CommonModule, BrowserModule ],

    declarations: [

        AppComponent,
        NavbarComponent,
        TwitComponent,
        PostComponent,

    ],

    providers: [
        PostService
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }