package khrom.test.sanzone.validation.annotation;

import khrom.test.sanzone.validation.CollectionNullValuesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by DEV on 10/3/2016.
 */
@Documented
@Target( { FIELD, ANNOTATION_TYPE } )
@Retention( RUNTIME )
@Constraint( validatedBy = CollectionNullValuesValidator.class )
public @interface NotNullElements {

    String message() default "Collection have null elements";

    Class< ? >[] groups() default {};

    Class< ? extends Payload>[] payload() default { };
}