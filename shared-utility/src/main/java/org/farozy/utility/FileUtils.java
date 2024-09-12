package org.farozy.utility;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    public static void deleteFile(String moduleName, String fileName) throws IOException {
        Path fileDir = getImageUploadDirectoryPath(moduleName);
        Path filePath = fileDir.resolve(fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new IOException("File not found: " + filePath);
        }
    }

    public static Path getImageUploadDirectoryPath(String moduleName) {
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path parentDir = currentDir.getParent();

        return Paths.get(parentDir.toUri())
                .resolve(moduleName)
                .resolve("src/main/resources/static/upload/image/");
    }

    public static Map<String, Object> getImageDetails(String moduleName, String imageName) throws IOException {
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path parentDir = currentDir.getParent();
        String srcDir = "src/main/resources/static/upload/image/";

        Path srcPath = Paths.get(parentDir.toUri())
                .resolve(moduleName)
                .resolve(srcDir);

        Path imagePath = Paths.get(String.valueOf(srcPath), imageName);

        Map<String, Object> imageMap = new HashMap<>();

        if (!Files.exists(imagePath)) {
            imageMap.put("file_name", imageName);
        } else {
            imageMap.put("file_name", imageName);
            imageMap.put("file_size", Files.size(imagePath));
            imageMap.put("file_type", Files.probeContentType(imagePath));
        }

        return imageMap;
    }

}
