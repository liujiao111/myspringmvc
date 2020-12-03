package com.liu.com.liu.annonation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRequestParam {

    String value();
}
