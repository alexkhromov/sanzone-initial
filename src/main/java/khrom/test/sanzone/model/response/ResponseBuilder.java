package khrom.test.sanzone.model.response;

import khrom.test.sanzone.model.response.enums.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by DEV on 9/12/2016.
 */
public class ResponseBuilder< T > {

    private SanzoneResponse< T > response;
    private HttpStatus code;

    public ResponseEntity< SanzoneResponse< T > > buildResponseEntity() {

        return new ResponseEntity< SanzoneResponse< T > >( response, code );
    }

    public static < T > ResponseBuilder< T > success() {

        return withEmptyResponse();
    }

    public static < T > ResponseBuilder< T > success( SanzoneResponse< T > response ) {

        return withResponse( response, ResponseStatus.SUCCESS );
    }

    public static < T > ResponseBuilder< T > failure() {

        return withEmptyResponse();
    }

    public static < T > ResponseBuilder< T > failure( SanzoneResponse< T > response ) {

        return withResponse( response, ResponseStatus.FAILURE );
    }

    public ResponseBuilder< T > code( HttpStatus code ) {

        this.code = code;
        return this;
    }

    public ResponseBuilder< T > data( List< T > data ) {

        if ( response != null ) {

            response.setData( data );
        }

        return this;
    }

    private static < T > ResponseBuilder< T > withEmptyResponse() {

        ResponseBuilder builder = new ResponseBuilder();

        SanzoneResponse response = null;

        builder.response = response;

        return builder;
    }

    private static < T > ResponseBuilder< T > withResponse( SanzoneResponse< T > response, ResponseStatus status ) {

        ResponseBuilder builder = new ResponseBuilder();

        builder.response = response;
        builder.response.setStatus( status );

        return builder;
    }
}