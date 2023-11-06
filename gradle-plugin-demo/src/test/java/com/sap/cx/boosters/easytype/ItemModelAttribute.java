package com.sap.cx.boosters.easytype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemModelAttribute {

    boolean optional() default true;

    boolean unique() default true;

    boolean initial() default true;

    boolean read() default true;

    boolean write() default true;

    boolean partof() default false;

    // TODO modifiers ...

}
