package org.jsp.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by  Jayesh S. Parkariya on 08-08-2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Field {

    String[] layout();

    FieldTypes fieldType() default FieldTypes.Private;

    public static final String startIndicator = "\n\t/*Declaration*/";

    public static final String endIndicator = "\n\t/*EndDeclaration*/";
}
