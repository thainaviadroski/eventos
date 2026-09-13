package net.eventos_facu.eventos.controllers;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.certificados.CertificadoImagemContentDto;
import net.eventos_facu.eventos.dto.certificados.CertificadoImagemResponseDto;
import net.eventos_facu.eventos.services.CertificadosImagesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/certficado-imagens")
public class CertificadoImagensController {

    private final CertificadosImagesService service;

    @GetMapping("/certificado/{certificadoId}")
    public List<CertificadoImagemResponseDto> findByCertificado(@PathVariable Long certificadoId) {
        return service.findByCertificadoId(certificadoId).stream()
                .map(image -> new CertificadoImagemResponseDto(image.getId(), image.getVerso(), image.getContentType()))
                .toList();
    }

    @PostMapping("/verso/{certificadoId}")
    public ResponseEntity<Void> addImageVerso(@PathVariable Long certificadoId, @RequestParam("file") MultipartFile file) throws IOException {
        service.createVersoImage(file, certificadoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{imagemId}")
    public ResponseEntity<byte[]> loadImage(@PathVariable Long imagemId) throws IOException {
        CertificadoImagemContentDto image = service.loadImage(imagemId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.contentType()))
                .body(image.content());
    }

    @PutMapping(value = "/{imagemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@PathVariable Long imagemId, @RequestPart MultipartFile fundo) throws IOException {
        service.updateImage(imagemId, fundo);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/{imagemId}/verso", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateVerso(@PathVariable Long imagemId, @RequestPart MultipartFile  verso) throws IOException {
        service.updateImage(imagemId, verso);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

    }
}
