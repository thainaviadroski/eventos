package net.eventos_facu.eventos.controllers;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.academicos.AcademicoRequestDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoResponseDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoUpdateDto;
import net.eventos_facu.eventos.services.AcademicoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/academico")
public class AcademicoController {

    private final Logger logger = LoggerFactory.getLogger(AcademicoController.class);

    private final AcademicoService service;


    @PostMapping
    public ResponseEntity<AcademicoResponseDto> createNewAcademico(@RequestBody AcademicoRequestDto academico) {
        AcademicoResponseDto result = service.createNewAcademico(academico);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.id()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping
    public Page<AcademicoResponseDto> getAllAcademico(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable)).getBody();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AcademicoResponseDto>> searchAcademicos(@RequestParam String q, Pageable pageable) {
        logger.info("Searching Academicos with term: {}", q);
        return ResponseEntity.ok(service.search(q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicoResponseDto> getAcademicoById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(service.findById(id));
    }

    @GetMapping("/by-cpf/{cpf}")
    public ResponseEntity<AcademicoResponseDto> getAcademicoByCpf(@PathVariable String cpf) {
        return ResponseEntity.ofNullable(service.findByCpf(cpf));
    }

    @PutMapping
    public ResponseEntity<AcademicoResponseDto> updateAcademico(@PathVariable Long id, @RequestBody AcademicoUpdateDto academico) {
        return ResponseEntity.ok(service.update(id, academico));
    }

}
