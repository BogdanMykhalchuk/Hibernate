package com.chapter07.validated.annotations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {QuadrantIIIValidator.class})
@Documented
public @interface NoQuadrantIII {
    String message() default "Failed quadrant III test";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
