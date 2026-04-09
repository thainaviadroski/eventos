package net.eventos_facu.eventos.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public byte[] loadFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found!");
        }
        return Files.readAllBytes(path);
    }

    public void saveFile(Path path, byte[] content) throws IOException {
        Path parentDir = path.getParent();

        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        Files.write(path, content);

        logger.info("Arquivo salvo com sucesso: {}", path.toAbsolutePath());
    }

    public void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

}
