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
import static khrom.test.sanzone.model.error.ErrorCode.FIELD_EMPTY;
import static khrom.test.sanzone.model.error.ErrorCode.FIELD_MISSING;

/**
 * Created by DEV on 10/3/2016.
 */
@Documented
@Target( FIELD )
@Retention( RUNTIME )
@Constraint( validatedBy = {} )
@NotNull( groups = {}, message = FIELD_MISSING )
@NotEmpty( groups = {}, message = FIELD_EMPTY )
public @interface ValidInput {

    String message() default "Not a valid property";

    Class< ? >[] groups() default {};

    Class< ? extends Payload>[] payload() default {};
}