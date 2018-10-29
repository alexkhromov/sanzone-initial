/**
 * Created by DEV on 21.03.2017.
 */
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';

import { AppComponent } from "./app.component";
import { DashboardComponent } from "./component/dashboard.component";
import { AntennaDetailComponent } from './component/antenna-detail.component';
import { AntennasComponent } from './component/antennas.component';
import { AntennaService } from './service/antenna.service';

import { AppRoutingModule } from './component/routing/app-routing.module';

// Imports for loading & configuring the in-memory web api
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService } from './service/in-memory-data.service';

import { PostComponent } from "./component/post.component";
import { TwitComponent } from "./component/twitcomponent/twit.component";
import { PostService } from "./service/post.service";
import { AntennaServiceMock } from "./service/antenna.service.mock";
import {
    MatCheckboxModule,
    MatMenuModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatGridListModule,
    MatCardModule,
    MatInputModule,

} from '@angular/material';
import { MatStepperModule } from '@angular/material/stepper';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SidenavComponent } from './component/sidenav/sidenav.component';
import { LayoutModule } from '@angular/cdk/layout';
import { DashboardcontentComponent } from './component/dashboardcontent/dashboardcontent.component';
import { StepperComponent } from './component/stepper/stepper.component';
import { ContactsComponent } from './component/contacts/contacts.component';



@NgModule({

    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpModule,
        InMemoryWebApiModule.forRoot(InMemoryDataService),
        CommonModule,
        MatCheckboxModule,
        MatMenuModule,
        BrowserAnimationsModule,
        LayoutModule,
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatIconModule,
        MatListModule,
        MatGridListModule,
        MatCardModule,
        MatInputModule,
        MatStepperModule
    ],

    declarations: [
        AppComponent,
        DashboardComponent,
        AntennaDetailComponent,
        AntennasComponent,
        TwitComponent,
        PostComponent,
        SidenavComponent,
        DashboardcontentComponent,
        StepperComponent,
        ContactsComponent,

    ],

    providers: [
        PostService,
        AntennaService,
        AntennaServiceMock
    ],

    bootstrap: [
        AppComponent
    ]
})

export class AppModule { }