package net.eventos_facu.eventos.services;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.entities.CertificadoImages;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.repositories.CertificadoImagesRepository;
import net.eventos_facu.eventos.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificadosImagesService {

    private final Logger logger = LoggerFactory.getLogger(CertificadosImagesService.class);

    private final CertificadoImagesRepository repository;

    private final FileUtils fileUtils;

    @Value("${app.upload.dir.certimage}")
    private String diretory;

    /*
     * Vamos validar se o certificado já possui uma imagem cadastrada se não houver permitir que salve uma imagem
     * Se houver validar se é o verso ou não
     * Se ja houver duas imagens um frente e um verso, retornar uma exception informando que o certificado já possui as imagens
     * */
    @Transactional
    public void createNewCertificadoImages(MultipartFile image, Certificados certificados) throws IOException {
        logger.info("Request save image certificado.id: {}", certificados.getId());
        String fileName = fileName(image.getOriginalFilename(), certificados.getId(), false);
        try {
            CertificadoImages entity = new CertificadoImages();
            entity.setCertificado(certificados);
            entity.setPath(fileName);
            entity.setCreated(LocalDateTime.now());
            entity.setContentType(image.getContentType());
            entity.setVerso(false);
            fileUtils.saveFile(Path.of(fileName), image.getBytes());
            save(entity);
        } catch (Exception e) {
            remove(Path.of(fileName));
        }
    }

    private CertificadoImages save(CertificadoImages image) {
        return repository.save(image);
    }

    private void remove(Path path) throws IOException {
        fileUtils.deleteFile(path);
    }

    private String fileName(String fileNameOriginal, Long certificadoId, boolean isVerso) {
        String extensionFile = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
        if (isVerso) {
            return diretory + File.separator + certificadoId + "-" + UUID.randomUUID().toString() + "-" + "verso" + extensionFile;
        }
        return diretory + File.separator + certificadoId + "-" + UUID.randomUUID().toString() + extensionFile;
    }

}
