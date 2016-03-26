package logics.databaseUtilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *This class is responsive to manage the Annotation for convert a table into a class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IDatabaseClass {
    /**
     * Set te column name of tha database
     * @return
     */
    String tableName() default "";
}
