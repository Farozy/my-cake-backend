package org.farozy.validation.annotation.image;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.farozy.validation.validator.image.ImageFileTypeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageFileTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFileType {

    String message() default "Invalid image file type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
