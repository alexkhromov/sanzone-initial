/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}        from '@angular/core';
import {CommonModule}    from '@angular/common';
import {BrowserModule}   from '@angular/platform-browser';
import {FormsModule}     from '@angular/forms';
import {AppComponent}    from "./app.component";
import {NavbarComponent} from "./navbar.component";
import {PostComponent}   from "./post.component";
import {TwitComponent}   from "./twit.component";
import {PostService}     from "../service/post.service";
import {DemoComponent}     from "./demo.component";

@NgModule( {

    imports: [ CommonModule, BrowserModule, FormsModule ],

    declarations: [

        AppComponent,
        NavbarComponent,
        TwitComponent,
        PostComponent,
        DemoComponent

    ],

    providers: [
        PostService
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }