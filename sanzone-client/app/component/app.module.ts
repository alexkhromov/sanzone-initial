/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}        from '@angular/core';
import {BrowserModule}   from '@angular/platform-browser';
import {FormsModule}     from '@angular/forms';
import {HttpModule  }    from '@angular/http';
import {CommonModule}    from '@angular/common';

import {AppComponent}    from "./app.component";
import {DashboardComponent} from "./dashboard.component";
import {AntennaDetailComponent} from './antenna-detail.component';
import {AntennasComponent}      from './antennas.component';
import {AntennaService}         from '../service/antenna.service';

import { AppRoutingModule }     from './app-routing.module';

// Imports for loading & configuring the in-memory web api
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService }  from '../service/in-memory-data.service';

import {NavbarComponent} from "./navbar.component";
import {PostComponent}   from "./post.component";
import {TwitComponent}   from "./twit.component";
import {PostService}     from "../service/post.service";
import {AntennaServiceMock}     from "../service/antenna.service.mock";




@NgModule( {

    imports: [  BrowserModule,
                FormsModule,
                AppRoutingModule,
                HttpModule,
                InMemoryWebApiModule.forRoot(InMemoryDataService),
                CommonModule
              ],

    declarations: [
                AppComponent,
                DashboardComponent,
                AntennaDetailComponent,
                AntennasComponent,
                NavbarComponent,
                TwitComponent,
                PostComponent
                  ],

    providers: [
        PostService,
        AntennaService,
        AntennaServiceMock
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }