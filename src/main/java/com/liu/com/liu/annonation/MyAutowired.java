package com.liu.com.liu.annonation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {

    String value();

}
