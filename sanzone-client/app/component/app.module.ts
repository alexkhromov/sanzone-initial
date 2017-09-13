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
import {DemoComponent}   from "./demo.component";
import {AntennaDetailComponent} from './antenna-detail.component';
import {AntennasComponent}      from './antennas.component';
import {AntennaService}         from './antenna.service';
import {BankService} from "../service/bank.service";
import {HttpClientModule} from "@angular/common/http";
import {ServerURLInterceptor} from "../service/server.url.interceptor";
import {InterceptorService} from "x-ng2-http-interceptor";
import {XHRBackend, RequestOptions, HttpModule} from "@angular/http";

export function interceptorFactory( xhrBackend: XHRBackend,
                                    requestOptions: RequestOptions,
                                    serverURLInterceptor: ServerURLInterceptor ) {

    let service = new InterceptorService( xhrBackend, requestOptions );
    service.addInterceptor( serverURLInterceptor );
    return service;
}

@NgModule( {

    imports: [

        CommonModule,
        BrowserModule,
        FormsModule,
        HttpClientModule,
        HttpModule,
        RouterModule.forRoot( [
            {
                path: 'antennas',
                component: AntennasComponent
            }
        ] )
    ],

    declarations: [

        AppComponent,
        NavbarComponent,
        TwitComponent,
        PostComponent,
        DemoComponent,
        AntennaDetailComponent,
        AntennasComponent

    ],

    providers: [

        PostService,
        AntennaService,
        BankService,
        ServerURLInterceptor,
        {
            provide: InterceptorService,
            useFactory: interceptorFactory,
            deps: [ XHRBackend, RequestOptions, ServerURLInterceptor ]
        }
    ],

    bootstrap: [
        AppComponent
    ]
} )

export class AppModule { }