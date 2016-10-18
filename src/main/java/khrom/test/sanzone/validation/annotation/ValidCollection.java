package khrom.test.sanzone.validation.annotation;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static khrom.test.sanzone.model.error.ErrorCode.*;

/**
 * Created by DEV on 10/3/2016.
 */
@Documented
@Target( FIELD )
@Retention( RUNTIME )
@Constraint( validatedBy = {} )
@NotNull( groups = {}, message = FIELD_MISSING )
@NotEmpty( groups = {}, message = COLLECTION_EMPTY )
@NotNullElements( groups = {}, message = COLLECTION_NULL_VALUES )
public @interface ValidCollection {

    String message() default "Not a valid collection";

    Class< ? >[] groups() default {};

    Class< ? extends Payload>[] payload() default {};
}