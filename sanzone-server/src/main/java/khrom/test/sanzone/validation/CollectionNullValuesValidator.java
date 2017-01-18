package khrom.test.sanzone.validation;

import khrom.test.sanzone.validation.annotation.NotNullElements;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created by DEV on 10/3/2016.
 */
public class CollectionNullValuesValidator implements ConstraintValidator< NotNullElements, Collection> {

    @Override
    public void initialize( NotNullElements constraintAnnotation ) {
    }

    @Override
    public boolean isValid( Collection collection, ConstraintValidatorContext context ) {

        if ( isEmpty( collection ) ) {
            return false;
        }

        for ( Object obj: collection ) {

            if ( obj == null ) {
                return false;
            }
        }

        return true;
    }
}