package net.eventos_facu.eventos.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoManualDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoResponseDto;
import net.eventos_facu.eventos.services.AcademicoCertificadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/academico-certificado")
public class AcademicoCertificadoController {

    private final Logger logger = LoggerFactory.getLogger(AcademicoCertificadoController.class);

    private final AcademicoCertificadoService service;

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AcademicoCertificadoResponseDto>> importFromFile(@RequestParam("certificadoId") Long certificadoId,
                                                                                  @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Request import academicos via arquivo, certificado.id: {}", certificadoId);
        return ResponseEntity.ok(service.importFromFile(certificadoId, file));
    }

    @PostMapping("/import/manual")
    public ResponseEntity<AcademicoCertificadoResponseDto> importManual(@RequestBody @Valid AcademicoCertificadoManualDto dto) {
        logger.info("Request import manual academico, certificado.id: {}", dto.certificadoId());
        return ResponseEntity.ok(service.importManual(dto));
    }
}
