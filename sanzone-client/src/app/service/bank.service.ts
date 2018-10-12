import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/map';
import {Response} from "@angular/http";

@Injectable()
export class BankService {

    constructor ( private http: HttpClient ) {}

    hello() {

        let result =  this.http.get( 'http://localhost:8080/client/' )
                        .map( ( res:Response ) => res );

        return result;
    }
}