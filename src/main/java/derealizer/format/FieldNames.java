package derealizer.format;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines field names for serialization formats.
 * This annotation is used to make serialization format definitions more readable.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface FieldNames {

	String[] value();

}
