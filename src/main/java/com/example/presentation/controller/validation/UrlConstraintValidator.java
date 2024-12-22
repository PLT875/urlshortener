package com.example.presentation.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;

@Slf4j
public class UrlConstraintValidator implements ConstraintValidator<ValidUrl, String> {
    private UrlValidator urlValidator;
    @Override
    public void initialize(ValidUrl constraintAnnotation) {
        String[] allowedSchemes = new String[]{"http", "https"};
        urlValidator = new UrlValidator(allowedSchemes);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return urlValidator.isValid(value);
    }
}
