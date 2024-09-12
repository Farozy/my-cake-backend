package org.farozy.validation.validator.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.farozy.validation.annotation.image.ImageFileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@NoArgsConstructor
public class ImageFileTypeValidator implements ConstraintValidator<ImageFileType, MultipartFile> {

    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpg", "image/jpeg", "image/png", "image/png", "image/gif"};

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) return true;

        String contentType = file.getContentType();
        return contentType != null && Arrays.asList(ALLOWED_IMAGE_TYPES).contains(contentType);
    }

}
