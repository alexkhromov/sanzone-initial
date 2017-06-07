/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';

@Component( {

    selector: 'my-app',

    template: `<navbarComponent></navbarComponent>
               <h4>Программное обеспечение Electro Magnetic Fields Software  (EMFs) предназначено для расчета санитарно-защитных зон и зон ограничения застройки по методикам и санитарным нормам действующих на территории Республики Беларусь.</h4>
               <bodyComponent></bodyComponent>
               <twit></twit>
               <footerComponent></footerComponent>

`
} )

export class AppComponent { }