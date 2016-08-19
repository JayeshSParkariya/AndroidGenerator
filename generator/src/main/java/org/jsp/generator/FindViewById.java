package org.jsp.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Jayesh S. Parkariya on 08-08-2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FindViewById {

    public static final String startIndicator = "\n\t\t/*Initialization*/";

    public static final String endIndicator = "\n\t\t/*EndInitialization*/";
}