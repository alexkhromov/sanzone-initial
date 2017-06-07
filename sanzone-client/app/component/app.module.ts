/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}      from '@angular/core';
import {CommonModule}      from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from "./app.component";
import {PostService} from "../service/post.service";
import {HeartComponent} from "./heart.component";
import {PostComponent} from "./post.component";
import {TwitComponent} from "./twit.component";
import {NavbarComponent} from "./navbar.component";
import {FooterComponent} from "./footer.component";
import {BodyComponent} from "./body.component";

@NgModule( {

    imports: [ CommonModule, BrowserModule ],

    declarations: [

        NavbarComponent,
        AppComponent,
        TwitComponent,
        PostComponent,
        HeartComponent,
        FooterComponent,
        BodyComponent
    ],

    providers: [
        PostService
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }