package org.farozy.helper;

import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.UUID;

public class FileUploadHelper {

    public static String processSaveImage(String moduleName, MultipartFile imageFile) {
        try {
//            String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
            return FileUploadHelper.saveFile(moduleName, imageFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file: " + e.getMessage(), e);
        }
    }

    private static String saveFile(String moduleName, MultipartFile file) throws IOException {
        Path resultsrcPath = createImageUploadDirectory(moduleName);

        String fileImageName = generateRandomFileName(file);
        Path filePath = resultsrcPath.resolve(fileImageName);

        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            writeParam.setCompressionQuality(0.8f);

            writer.setOutput(new FileImageOutputStream(filePath.toFile()));
            writer.write(null, new IIOImage(image, null, null), writeParam);

            return filePath.getFileName().toString();
        } catch (Exception e) {
            System.out.println("Error saving image as WebP: " +  e);
        }

//      Files.copy(file.getInputStream(), filePath);

        return fileImageName;
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
        String fileExtension = ".webp";
        return generateRandomFileName + fileExtension;
    }

}
