export class ClientResponse {

    status: string;
    redirect?: string;
    data?: any;

    constructor( clientResponse: any ) {

        if ( clientResponse ) {

            this.status = clientResponse.status;
            this.redirect = clientResponse.redirect;
            this.data = clientResponse.data;
        }
    }
}