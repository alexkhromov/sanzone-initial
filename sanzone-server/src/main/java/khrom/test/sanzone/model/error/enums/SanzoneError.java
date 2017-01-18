package khrom.test.sanzone.model.error.enums;

import khrom.test.sanzone.model.error.ErrorCode;

/**
 * Created by DEV on 10/3/2016.
 */
public enum SanzoneError {

    FIELD_MISSING( ErrorCode.FIELD_MISSING, "'%s' field is missing" ),
    FIELD_EMPTY( ErrorCode.FIELD_EMPTY, "'%s' field is empty" ),
    FIELD_RANGE( ErrorCode.FIELD_RANGE, "'%s' field is out of range [ %s; %s ]" ),
    FIELD_INVALID( ErrorCode.FIELD_INVALID, "'%s' field have invalid value [ %s ]" ),

    COLLECTION_EMPTY( ErrorCode.COLLECTION_EMPTY, "'%s' collection is empty" ),
    COLLECTION_NULL_VALUES( ErrorCode.COLLECTION_NULL_VALUES, "'%s' collection have null elements" ),

    INCOMPATIBLE( ErrorCode.INCOMPATIBLE, "incompatible properties: '%s'" ),

    UNEXPECTED( null, "unexpected error: '%s'" );

    private String message;
    private String code;

    SanzoneError( String code, String message ) {

        this.code = code;
        this.message = message;
    }

    public String format( String... parameters ) {

        return String.format( this.message, parameters );
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public static SanzoneError byCode( String code ) {

        if ( code == null )
            return UNEXPECTED;

        for ( SanzoneError err : values() ) {

            if ( code.equals( err.code ) ) {
                return err;
            }
        }

        return UNEXPECTED;
    }
}