package org.farozy.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
@Setter
public class FileUploadProperties {
    private int maxProductImage = 5;
}