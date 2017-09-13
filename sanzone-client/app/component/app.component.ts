import {Component} from '@angular/core';
import {BankService} from "../service/bank.service";

@Component( {

    selector: 'my-app',

    template: `<navbarComponent></navbarComponent>
               
               <div class="container-fluid" style="padding-top: 50px">

                    <div class="row">
                        <h2><span class="label label-success col-md-8 col-md-offset-2">Bank Page</span></h2>
                    </div>
                
                    <div class="row" style="padding-top: 30px">
                        <div class="col-md-4 col-md-offset-4">
                            <p class="form-control-static">Amount for transfer:</p>
                            <div class="input-group input-group-lg">
                                <input type="text" class="form-control text-center input-lg" placeholder="1000" id="amount" [(ngModel)]="amount">
                                <span class="input-group-btn">
                                    <button type="submit" class="btn btn-success" (click)="transfer()">Transfer</button>
                                </span>
                            </div>
                        </div>
                    </div>
                
                    <div *ngIf="isClicked" class="row" style="padding-top: 30px">
                        <h4><span class="label label-success text-center col-md-4 col-md-offset-4">{{info}}</span></h4>
                    </div>
                
                </div>`
            } )

export class AppComponent {

    constructor( private bankService: BankService ) { }

    amount: string;
    isClicked = false;
    info: String;

    transfer() {

        this.bankService.transfer( 'user', this.amount ).subscribe( data => {

            this.isClicked = true;
            this.info = data.info;
            this.amount = null;
        } );
    }
}