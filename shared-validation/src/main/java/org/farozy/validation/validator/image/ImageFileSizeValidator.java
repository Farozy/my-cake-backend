package org.farozy.validation.validator.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.farozy.validation.annotation.image.ImageFileSize;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileSizeValidator implements ConstraintValidator<ImageFileSize, MultipartFile> {
    private static final long MAX_FILE_SIZE = 1048576; // 1MB in bytes

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) return true;

        boolean valid = file.getSize() <= MAX_FILE_SIZE;

        if (!valid) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
//        return value.getSize() <= MAX_FILE_SIZE;
    }
}
