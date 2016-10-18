package khrom.test.sanzone.model.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import khrom.test.sanzone.model.response.ResponseBuilder;
import khrom.test.sanzone.model.response.SanzoneResponse;
import khrom.test.sanzone.model.error.enums.SanzoneError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static khrom.test.sanzone.model.error.enums.SanzoneError.FIELD_RANGE;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by DEV on 10/2/2016.
 */
@ControllerAdvice
public class ErrorHandler {

    private static final String INVALID_JSON = "some problems with json format";
    private static final String UNEXPECTED_ERROR = "see description";

    @ExceptionHandler( HttpMessageNotReadableException.class )
    @ResponseStatus( BAD_REQUEST )
    @ResponseBody
    public ResponseEntity handle( HttpMessageNotReadableException e ) {

        if ( getRootCause( e ) instanceof InvalidFormatException) {
            return handle( ( InvalidFormatException ) getRootCause( e ) );
        } else {

            SanzoneError se = SanzoneError.UNEXPECTED;

            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage( se.format( INVALID_JSON ) );
            errorMessage.setDescription( getRootCause( e ).getMessage() );

            SanzoneResponse< ErrorMessage > response = new SanzoneResponse<>();
            response.addData( errorMessage );

            return ResponseBuilder.failure( response ).code( BAD_REQUEST ).buildResponseEntity();
        }
    }

    @ExceptionHandler( MethodArgumentNotValidException.class )
    @ResponseStatus( BAD_REQUEST )
    @ResponseBody
    public ResponseEntity handle( MethodArgumentNotValidException ex ) {

        SanzoneResponse< ErrorMessage > response = new SanzoneResponse<>();

        ex.getBindingResult().getAllErrors().stream().forEach( error -> {

            ErrorMessage errorMessage = new ErrorMessage();

            if ( error instanceof FieldError ) {

                FieldError fe = ( FieldError ) error;

                SanzoneError se = SanzoneError.byCode( fe.getDefaultMessage() );

                errorMessage.setField( fe.getField() );
                errorMessage.setMessage( se.format( pureFieldName( fe.getField() ),
                                                    FIELD_RANGE.equals( se ) ? fe.getArguments()[ 2 ].toString() : null,
                                                    FIELD_RANGE.equals( se ) ? fe.getArguments()[ 1 ].toString() : null ) );
            }

            response.addData( errorMessage );
        } );

        return ResponseBuilder.failure( response ).code( BAD_REQUEST ).buildResponseEntity();
    }

    @ExceptionHandler( Exception.class )
    @ResponseStatus( INTERNAL_SERVER_ERROR )
    @ResponseBody
    public ResponseEntity handle( Exception e ) {

        SanzoneError se = SanzoneError.UNEXPECTED;

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage( se.format( UNEXPECTED_ERROR ) );
        errorMessage.setDescription( getRootCause( e ).getMessage() );

        SanzoneResponse< ErrorMessage > response = new SanzoneResponse<>();
        response.addData( errorMessage );

        return ResponseBuilder.failure( response ).code( INTERNAL_SERVER_ERROR ).buildResponseEntity();
    }

    private String pureFieldName( String field ) {

        if ( field.indexOf( '.' ) == -1 ) {
            return field;
        }

        return field.substring( field.lastIndexOf( '.' ) + 1 );
    }
}