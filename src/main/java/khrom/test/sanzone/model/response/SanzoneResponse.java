package khrom.test.sanzone.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import khrom.test.sanzone.model.response.enums.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created by DEV on 9/12/2016.
 */
@JsonInclude( NON_NULL )
@JsonPropertyOrder( { "status", "data" } )
public class SanzoneResponse< T > {

    private List< T > data;
    private ResponseStatus status;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public void addData( T element ) {

        if ( data == null ) {
            data = new ArrayList<>();
        }

        data.add( element );
    }
}