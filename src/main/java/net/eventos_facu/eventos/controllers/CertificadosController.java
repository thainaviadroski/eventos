package net.eventos_facu.eventos.controllers;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.certificados.CertificadoRequestDto;
import net.eventos_facu.eventos.dto.certificados.CertificadoResponseDto;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.services.CertificadosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/certificados")
public class CertificadosController {

    private Logger logger = LoggerFactory.getLogger(CertificadosController.class);

    private final CertificadosService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificadoResponseDto> createNewCertificado(@RequestPart("certificado") CertificadoRequestDto certificado, @RequestPart("fundo") MultipartFile fundo) throws IOException {
        logger.info("Request new certificado: {}", certificado.toString());
        CertificadoResponseDto result = service.createNewCertificado(certificado, fundo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.id()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping
    public Page<CertificadoResponseDto> getAllCertificados(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable)).getBody();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificadoResponseDto> getCertificadoById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<CertificadoResponseDto>> getCertificadoByEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(service.findByEventoId(eventoId));
    }

    @PutMapping
    public void updateCertificadoById(@RequestBody Certificados c) {
    }

    @DeleteMapping("/{id}")
    public void deleteCertificadoById(@PathVariable Long id) {
        service.remove(id);
    }
}
