package net.eventos_facu.eventos.services;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoManualDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoResponseDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoRequestDto;
import net.eventos_facu.eventos.entities.Academicos;
import net.eventos_facu.eventos.entities.AcademicosCertificados;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.exception.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.AcademicoCertificadoMapper;
import net.eventos_facu.eventos.mapper.AcademicoMapper;
import net.eventos_facu.eventos.repositories.AcademicosCertificadosRepository;
import net.eventos_facu.eventos.repositories.AcademicosRepository;
import net.eventos_facu.eventos.repositories.CertificadosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicoCertificadoService {

    private final Logger logger = LoggerFactory.getLogger(AcademicoCertificadoService.class);

    private final AcademicosCertificadosRepository repository;
    private final AcademicosRepository academicosRepository;
    private final CertificadosRepository certificadosRepository;
    private final AcademicoMapper academicoMapper;
    private final AcademicoCertificadoMapper mapper;

    @Transactional
    public AcademicoCertificadoResponseDto importManual(AcademicoCertificadoManualDto dto) {
        logger.info("Importando manualmente academico no certificado.id: {}", dto.certificadoId());
        Certificados certificado = findCertificado(dto.certificadoId());
        Academicos academico = resolveAcademico(dto.academico());
        AcademicosCertificados entity = link(academico, certificado, dto.ch());
        return mapper.toDtoResponse(repository.save(entity));
    }

    @Transactional
    public List<AcademicoCertificadoResponseDto> importFromFile(Long certificadoId, MultipartFile file) throws IOException {
        logger.info("Importando academicos via arquivo para certificado.id: {}", certificadoId);
        Certificados certificado = findCertificado(certificadoId);
        List<AcademicoCertificadoResponseDto> imported = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                AcademicosCertificados entity = parseLine(line, lineNumber, certificado);
                imported.add(mapper.toDtoResponse(repository.save(entity)));
            }
        }

        return imported;
    }

    private AcademicosCertificados parseLine(String line, int lineNumber, Certificados certificado) {
        String[] columns = line.split(",", -1);
        if (columns.length < 5) {
            throw new IllegalArgumentException("Linha " + lineNumber + " inválida, formato esperado: academico,email,cpf,ra,ch");
        }

        AcademicoRequestDto academicoDto = new AcademicoRequestDto(
                columns[0].trim(),
                columns[1].trim(),
                columns[2].trim(),
                columns[3].trim(),
                null,
                null
        );

        Integer ch;
        try {
            ch = Integer.valueOf(columns[4].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Linha " + lineNumber + " possui carga horária inválida: " + columns[4]);
        }

        Academicos academico = resolveAcademico(academicoDto);
        return link(academico, certificado, ch);
    }

    private Academicos resolveAcademico(AcademicoRequestDto dto) {
        return academicosRepository.findByCpfOrRa(dto.cpf(), dto.ra())
                .orElseGet(() -> academicosRepository.save(academicoMapper.toEntity(dto)));
    }

    private Certificados findCertificado(Long certificadoId) {
        return certificadosRepository.findById(certificadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado não encontrado com id: " + certificadoId));
    }

    private AcademicosCertificados link(Academicos academico, Certificados certificado, Integer ch) {
        AcademicosCertificados entity = new AcademicosCertificados();
        entity.setAcademico(academico);
        entity.setCertificado(certificado);
        entity.setCh(ch);
        entity.setCreated(Instant.now());
        return entity;
    }
}
