package net.eventos_facu.eventos.controllers;


import net.eventos_facu.eventos.dto.certificados.CertificadoRequestDto;
import net.eventos_facu.eventos.entities.Certificados;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/certificados")
public class CertificadosController {

    private Logger logger = LoggerFactory.getLogger(CertificadosController.class);

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createNewCertificado(@RequestPart CertificadoRequestDto certificado,
                                     @RequestPart("fundo") MultipartFile fundo,
                                     @RequestPart("verso") MultipartFile verso) {
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
