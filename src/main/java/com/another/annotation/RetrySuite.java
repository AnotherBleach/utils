package com.another.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetrySuite {

    Class<? extends Predicate> retryChecker() default Predicate.class;


    Class<?>[] expectedException() default {RuntimeException.class};

    int maxRetryTimes() default 3;


}
