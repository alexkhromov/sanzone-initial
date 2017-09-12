/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}        from '@angular/core';
import {CommonModule}    from '@angular/common';
import {BrowserModule}   from '@angular/platform-browser';
import {FormsModule}     from '@angular/forms';
import {RouterModule}    from '@angular/router';

import {AppComponent}    from "./app.component";
import {NavbarComponent} from "./navbar.component";
import {PostComponent}   from "./post.component";
import {TwitComponent}   from "./twit.component";
import {PostService}     from "../service/post.service";
import {AntennaDetailComponent} from './antenna-detail.component';
import {AntennasComponent}      from './antennas.component';
import {AntennaService}         from './antenna.service';
import {DashboardComponent} from "./dashboard.component";


@NgModule( {

    imports: [ CommonModule,
               BrowserModule,
               FormsModule,
               RouterModule.forRoot([
                   {path: 'main', component: TwitComponent},
                   {path: 'antennas', component: AntennasComponent},
                   {path: 'dashboard', component: DashboardComponent},
                   {path: 'detail/:id',component: AntennaDetailComponent},
                   {path: '', redirectTo: '/main', pathMatch: 'full'},

               ])
    ],

    declarations: [

        AppComponent,
        NavbarComponent,
        TwitComponent,
        PostComponent,
        AntennaDetailComponent,
        AntennasComponent,
        DashboardComponent
    ],

    providers: [
        PostService,
        AntennaService
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }