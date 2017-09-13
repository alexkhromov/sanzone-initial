export class TransactionResponse {

    info: string;

    constructor( transactionResponse: any ) {

        if ( transactionResponse ) {

            this.info = transactionResponse.info;
        }
    }
}