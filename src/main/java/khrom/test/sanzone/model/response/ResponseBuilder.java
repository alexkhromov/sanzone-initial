package khrom.test.sanzone.model.response;

import khrom.test.sanzone.model.response.enums.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static khrom.test.sanzone.model.response.enums.ResponseStatus.FAILURE;
import static khrom.test.sanzone.model.response.enums.ResponseStatus.SUCCESS;

/**
 * Created by DEV on 9/12/2016.
 */
public class ResponseBuilder< T > {

    private SanzoneResponse< T > response;
    private HttpStatus code;

    public ResponseEntity< SanzoneResponse< T > > buildResponseEntity() {

        return new ResponseEntity<>( response, code );
    }

    public static < T > ResponseBuilder< T > success() {

        return withEmptyData( SUCCESS );
    }

    public static < T > ResponseBuilder< T > success( T data ) {

        SanzoneResponse< T > response = new SanzoneResponse<>();
        response.addData( data );

        return withData( response, SUCCESS );
    }

    public static < T > ResponseBuilder< T > failure() {

        return withEmptyData( FAILURE );
    }

    public static < T > ResponseBuilder< T > failure( SanzoneResponse< T > response ) {

        return withData( response, FAILURE );
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

    private static < T > ResponseBuilder< T > withEmptyData( ResponseStatus status ) {

        ResponseBuilder builder = new ResponseBuilder();

        SanzoneResponse response = new SanzoneResponse();

        builder.response = response;
        builder.response.setStatus( status );

        return builder;
    }

    private static < T > ResponseBuilder< T > withData( SanzoneResponse< T > response, ResponseStatus status ) {

        ResponseBuilder builder = new ResponseBuilder();

        builder.response = response;
        builder.response.setStatus( status );

        return builder;
    }
}