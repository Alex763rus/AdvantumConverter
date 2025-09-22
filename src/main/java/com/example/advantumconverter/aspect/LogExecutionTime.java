package com.example.advantumconverter.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    String value() default "";

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    @AllArgsConstructor
    @Getter
    enum TimeUnit {

        NANOSECONDS("%,d нс", 1.0),
        MICROSECONDS("%,.2f мкс", 1000.0),
        MILLISECONDS("%,.2f мс", 1_000_000.0),
        SECONDS("%,.3f с", 1_000_000_000.0);

        private final String format;
        private final double dividend;
    }
}