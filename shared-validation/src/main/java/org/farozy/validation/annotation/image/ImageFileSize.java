package org.farozy.validation.annotation.image;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.farozy.validation.validator.image.ImageFileSizeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageFileSizeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFileSize {
    String message() default "File size is too large";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
