/**
 * Created by DEV on 21.03.2017.
 */
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from "../dashboard.component";
import { AntennasComponent } from '../antennas.component';
import { AntennaDetailComponent } from '../antenna-detail.component';
import { TwitComponent } from "../twitcomponent/twit.component";

const routes: Routes = [
    { path: '', redirectTo: '/main', pathMatch: 'full' },
    { path: 'main', component: TwitComponent },
    { path: 'antennas', component: AntennasComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'detail/:id', component: AntennaDetailComponent }

];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }