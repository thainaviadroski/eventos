package net.eventos_facu.eventos.controllers;


import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.certificados.CertificadoRequestDto;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.services.CertificadosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/certificados")
public class CertificadosController {

    private Logger logger = LoggerFactory.getLogger(CertificadosController.class);

    private final CertificadosService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createNewCertificado(@RequestPart("certificado") CertificadoRequestDto certificado,
                                     @RequestPart("fundo") MultipartFile fundo) {
        logger.info("Info certificado: {}", certificado.toString());
        logger.info("Imagem:{}", fundo.getOriginalFilename());

        try {
            service.createNewCertificado(certificado, fundo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping
    public List<Certificados> getAllCertificados() {
        return null;
    }

    @GetMapping("/{id}")
    public void getCertificadoById(@PathVariable Long id) {
    }

    @PutMapping
    public void updateCertificadoById(@RequestBody Certificados c) {
    }

    @DeleteMapping
    public void deleteCertificadoById(@RequestBody Certificados c) {
    }
}
