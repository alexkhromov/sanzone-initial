/**
 * Created by DEV on 21.03.2017.
 */
import {NgModule}        from '@angular/core';
import {RouterModule}    from '@angular/router';
import {AntennaDetailComponent} from './antenna-detail.component';
import {AntennasComponent}      from './antennas.component';
import {DashboardComponent} from "./dashboard.component";
import {TwitComponent}   from "./twit.component";


const routes: Routes = [
                   {path: '', redirectTo: '/main', pathMatch: 'full'},
                   {path: 'main', component: TwitComponent},
                   {path: 'antennas', component: AntennasComponent},
                   {path: 'dashboard', component: DashboardComponent},
                   {path: 'detail/:id',component: AntennaDetailComponent}


    ];


@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}