package com.chapter07.validated.annotations;


import com.chapter07.validated.Coordinate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class QuadrantIIIValidator implements ConstraintValidator<NoQuadrantIII, Coordinate>
{
    @Override
    public void initialize(NoQuadrantIII constraintAnnotation) {
    }
    @Override
    public boolean isValid(Coordinate value, ConstraintValidatorContext context) {
        return !(value.getX() < 0 && value.getY() < 0);
    }
}
