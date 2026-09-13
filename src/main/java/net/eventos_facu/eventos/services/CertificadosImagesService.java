package net.eventos_facu.eventos.services;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.certificados.CertificadoImagemContentDto;
import net.eventos_facu.eventos.entities.CertificadoImages;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.exception.ResourceNotFoundException;
import net.eventos_facu.eventos.repositories.CertificadoImagesRepository;
import net.eventos_facu.eventos.repositories.CertificadosRepository;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificadosImagesService {

    private final Logger logger = LoggerFactory.getLogger(CertificadosImagesService.class);

    private final CertificadoImagesRepository repository;

    private final CertificadosRepository certificadosRepository;

    private final FileUtils fileUtils;

    @Value("${app.upload.dir.certimage}")
    private String diretory;

    /*
     * Vamos validar se o certificado já possui uma imagem cadastrada se não houver permitir que salve uma imagem
     * Se houver validar se é o verso ou não
     * Se ja houver duas imagens um frente e um verso, retornar uma exception informando que o certificado já possui as imagens
     */
    @Transactional
    public void createNewCertificadoImages(MultipartFile image, Certificados certificados) throws IOException {
        createImage(image, certificados, false);
    }

    @Transactional
    public void createVersoImage(MultipartFile image, Long certificadoId) throws IOException {
        Certificados certificado = certificadosRepository.findById(certificadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado não encontrado com id: " + certificadoId));
        createImage(image, certificado, true);
    }

    private void createImage(MultipartFile image, Certificados certificados, boolean verso) throws IOException {
        logger.info("Request save image certificado.id: {}, verso: {}", certificados.getId(), verso);
        String fileName = fileName(image.getOriginalFilename(), certificados.getId(), verso);
        try {
            CertificadoImages entity = new CertificadoImages();
            entity.setCertificado(certificados);
            entity.setPath(fileName);
            entity.setCreated(LocalDateTime.now());
            entity.setContentType(image.getContentType());
            entity.setVerso(verso);
            fileUtils.saveFile(Path.of(fileName), image.getBytes());
            save(entity);
        } catch (Exception e) {
            removeFile(Path.of(fileName));
        }
    }

    private CertificadoImages save(CertificadoImages image) {
        return repository.save(image);
    }

    @Transactional(readOnly = true)
    public CertificadoImagemContentDto loadImage(Long imagemId) throws IOException {
        logger.info("Carregando imagem.id: {}", imagemId);
        CertificadoImages entity = findEntityById(imagemId);
        byte[] content = fileUtils.loadFile(Path.of(entity.getPath()));
        return new CertificadoImagemContentDto(content, entity.getContentType());
    }

    @Transactional
    public void updateImage(Long imagemId, MultipartFile file) throws IOException {
        logger.info("Atualizando imagem.id: {}", imagemId);
        CertificadoImages entity = findEntityById(imagemId);
        String oldPath = entity.getPath();
        String newFileName = fileName(file.getOriginalFilename(), entity.getCertificado().getId(), Boolean.TRUE.equals(entity.getVerso()));

        fileUtils.saveFile(Path.of(newFileName), file.getBytes());
        entity.setPath(newFileName);
        entity.setContentType(file.getContentType());
        save(entity);

        try {
            removeFile(Path.of(oldPath));
        } catch (IOException e) {
            logger.warn("Não foi possível remover o arquivo antigo: {}", oldPath, e);
        }
    }

    private CertificadoImages findEntityById(Long imagemId) {
        return repository.findById(imagemId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagem não encontrada com id: " + imagemId));
    }

    public void remove(Long certificadoId) {
        List<CertificadoImages> imgs = repository.findByCertificadoId(certificadoId);

        imgs.forEach(img -> {
            try {
                removeFile(Path.of(img.getPath()));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        imgs.forEach(repository::delete);
        imgs.clear();
    }

    private void removeFile(Path path) throws IOException {
        fileUtils.deleteFile(path);
    }

    private String fileName(String fileNameOriginal, Long certificadoId, boolean isVerso) {
        String extensionFile = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
        if (isVerso) {
            return diretory + File.separator + certificadoId + "-" + UUID.randomUUID().toString() + "-" + "verso" + extensionFile;
        }
        return diretory + File.separator + certificadoId + "-" + UUID.randomUUID().toString() + extensionFile;
    }

    public List<CertificadoImages> findByCertificadoId(Long certificadoId) {
        return repository.findByCertificadoId(certificadoId);
    }
}
