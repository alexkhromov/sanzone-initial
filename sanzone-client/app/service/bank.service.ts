import {Injectable} from '@angular/core';
import {URLSearchParams} from '@angular/http';
import 'rxjs/add/operator/map';
import {InterceptorService} from "x-ng2-http-interceptor";
import {ClientResponse} from "../model/client.response";
import {TransactionResponse} from "../model/transaction.response";

@Injectable()
export class BankService {

    constructor ( private http: InterceptorService ) { }

    transfer( account: string, amount: string ) {

        let params = new URLSearchParams();
        params.set( 'account', account );
        params.set( 'amount', amount );

        return this.http.post( 'http://localhost:8080/client/api/transfer', null, { search: params } )
            .map( res => {

                let clientResponse = < ClientResponse > res.json();

                return < TransactionResponse > clientResponse.data;
            } );
    }
}