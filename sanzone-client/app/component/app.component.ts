/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';
import {BankService} from "../service/bank.service";

@Component( {

    selector: 'my-app',

    template: `<navbarComponent></navbarComponent>
               
               <div class="container-fluid" style="padding-top: 50px">

                    <div class="row">
                        <h2><span class="label label-success col-md-8 col-md-offset-2">Bank Page</span></h2>
                    </div>
                
                    <div class="row form-inline col-md-4 col-md-offset-4" style="padding-top: 30px">
                        <label class="label label-success" for="amount">Amount</label>
                        <input type="text" class="form-control text-center" id="amount" placeholder="1000">
                        <button type="submit" class="btn btn-success" (click)="hello()">Transfer</button>
                    </div>
                
                    <div *ngIf="isClicked" class="row" style="padding-top: 30px">
                        <h4><span class="label label-success col-md-2 col-md-offset-5">{{greeting}}</span></h4>
                    </div>
                
                </div>`
            } )

export class AppComponent {readonly

    constructor( private bankService: BankService ) {}
    isClicked = false;
    greeting;

    hello() {

        this.isClicked = true;

        this.bankService.hello().subscribe( data => {

            this.greeting = data;
            console.log( data )
        } );
    }
}