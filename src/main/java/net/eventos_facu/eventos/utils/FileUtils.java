package net.eventos_facu.eventos.utils;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileUtils {

    byte[] loadFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found!");
        }
        return Files.readAllBytes(path);
    }

    void saveFile(Path path, byte[] content) throws IOException {
        Files.write(path, content);
    }

    void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

}
