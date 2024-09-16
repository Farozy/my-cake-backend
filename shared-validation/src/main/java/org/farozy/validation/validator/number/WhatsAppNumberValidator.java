package org.farozy.validation.validator.number;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.farozy.validation.annotation.number.ValidWhatsAppNumber;

public class WhatsAppNumberValidator implements ConstraintValidator<ValidWhatsAppNumber, String> {

    private static final String INDONESIAN_WHATSAPP_REGEX = "^(\\+628|08)[1-9][0-9]{7,11}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return phoneNumber.matches(INDONESIAN_WHATSAPP_REGEX);
    }
}
