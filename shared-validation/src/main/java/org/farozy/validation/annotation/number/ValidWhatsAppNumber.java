package org.farozy.validation.annotation.number;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.farozy.validation.validator.number.WhatsAppNumberValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WhatsAppNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWhatsAppNumber {
    String message() default "Invalid WhatsApp number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
