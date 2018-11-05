import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { AppComponent } from "./app.component";
import { DashboardComponent } from "./component/dashboard/dashboard.component";
import { AntennaDetailComponent } from './component/antenna-detail/antenna-detail.component';
import { AntennasComponent } from './component/antennas/antennas.component';
import { AntennaService } from './service/antenna.service';

import { AppRoutingModule } from './component/routing/app-routing.module';

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
  MatTableModule,
  MatTabsModule,
  MatExpansionModule,
  MatProgressSpinnerModule

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
    HttpClientModule,
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
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatExpansionModule,
    MatProgressSpinnerModule
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
