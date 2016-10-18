package khrom.test.sanzone.model.error;

/**
 * Created by DEV on 10/3/2016.
 */
public interface ErrorCode {

    String FIELD_MISSING = "field.missing";
    String FIELD_EMPTY = "field.empty";
    String FIELD_RANGE = "field.range";

    String COLLECTION_EMPTY = "collection.empty";
    String COLLECTION_NULL_VALUES = "collection.null.values";

    String INCOMPATIBLE = "incompatible";
}