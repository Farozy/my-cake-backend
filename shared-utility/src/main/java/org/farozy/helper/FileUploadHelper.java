package org.farozy.helper;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class FileUploadHelper {

    public static String processSaveImage(String moduleName, MultipartFile imageFile) {
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
            return FileUploadHelper.saveFile(moduleName, fileName, imageFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file: " + e.getMessage(), e);
        }
    }

    private static String saveFile(String moduleName, String fileName, MultipartFile file) throws IOException {
        Path resultsrcPath = createImageUploadDirectory(moduleName);

        try {
            String fileImageName = generateRandomFileName(file);
            Path filePath = resultsrcPath.resolve(fileImageName);

            BufferedImage image = ImageIO.read(file.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "webp", baos);
            byte[] webpData = baos.toByteArray();

            try (FileOutputStream fos = new FileOutputStream(String.valueOf(filePath))) {
                fos.write(webpData);
            }




//            Files.copy(file.getInputStream(), filePath);

            return fileImageName;
        } catch (IOException ex) {
            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public static String saveImageLocally(String imageUrl, String imageName) throws MalformedURLException, URISyntaxException {
        Path resultsrcPath = createImageUploadDirectory("user-module");

        Path filePath = resultsrcPath.resolve(imageName);

        URI uri = new URI(imageUrl);
        URL url = uri.toURL();

        try (InputStream in = url.openStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image locally", e);
        }

        return imageName;
    }

    private static Path createImageUploadDirectory(String moduleName) {
        if (moduleName == null || moduleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Module name must not be null or empty");
        }

        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path parentDir = currentDir.getParent();

        Path srcPath = Paths.get(parentDir.toUri())
                .resolve(moduleName)
                .resolve("src/main/resources/static/upload/image/");

        try {
            Files.createDirectories(srcPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }

        return srcPath;
    }

    private static String generateRandomFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String generateRandomFileName = UUID.randomUUID().toString();
        return generateRandomFileName + ".webp";
    }

}
