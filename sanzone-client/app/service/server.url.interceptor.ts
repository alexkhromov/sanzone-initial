import {Injectable} from "@angular/core";
import {
    Interceptor, InterceptorResponseWrapper,
    InterceptorRequest
} from "x-ng2-http-interceptor";
import {Observable} from "rxjs";
import {ClientResponse} from "../model/client.response";

@Injectable()
export class ServerURLInterceptor implements Interceptor {

    beforeRequest( request: InterceptorRequest ): Observable< InterceptorRequest > | InterceptorRequest | void {

        if ( request.url.toString().indexOf( "/api/" ) !== -1 ) {

            request.options.headers.append( 'X-Requested-With', 'XMLHttpRequest' )
            request.options.withCredentials = true;
        }

        return request;
    }

    onResponse( responseWrapper: InterceptorResponseWrapper,
                interceptorStep?: number )
                : Observable< InterceptorResponseWrapper > | InterceptorResponseWrapper | void {

        let clientResponse = < ClientResponse > responseWrapper.response.json();

        if ( clientResponse.status === "302" ) {
            window.location.href = clientResponse.redirect + "?redirect_uri=" + window.location.href;
        }

        return responseWrapper;
    }
}