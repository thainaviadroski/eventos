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
import net.eventos_facu.eventos.utils.CpfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

        for (CsvRow row : readCsv(file)) {
            Academicos academico = resolveAcademico(row.academico());
            // Também pega o mesmo acadêmico repetido no arquivo com dados diferentes:
            // a query faz flush dos vínculos salvos nas linhas anteriores.
            if (repository.existsByAcademicoIdAndCertificadoId(academico.getId(), certificadoId)) {
                logger.info("Academico.id {} já vinculado ao certificado.id {}, ignorando", academico.getId(), certificadoId);
                continue;
            }
            AcademicosCertificados entity = link(academico, certificado, row.ch());
            imported.add(mapper.toDtoResponse(repository.save(entity)));
        }

        return imported;
    }

    @Transactional(readOnly = true)
    public Page<AcademicoCertificadoResponseDto> findByAcademicoId(Long academicoId, Pageable pageable) {
        logger.info("Buscando certificados do academico.id: {}", academicoId);
        if (!academicosRepository.existsById(academicoId)) {
            throw new ResourceNotFoundException("Acadêmico não encontrado com id: " + academicoId);
        }
        return repository.findByAcademicoId(academicoId, pageable).map(mapper::toDtoResponse);
    }

    @Transactional(readOnly = true)
    public Page<AcademicoCertificadoResponseDto> findByCertificadoId(Long certificadoId, Pageable pageable) {
        logger.info("Buscando academicos do certificado.id: {}", certificadoId);
        if (!certificadosRepository.existsById(certificadoId)) {
            throw new ResourceNotFoundException("Certificado não encontrado com id: " + certificadoId);
        }
        return repository.findByCertificadoId(certificadoId, pageable).map(mapper::toDtoResponse);
    }

    @Transactional
    public void removeAcademico(Long certificadoId, Long academicoId) {
        logger.info("Removendo academico.id {} do certificado.id {}", academicoId, certificadoId);
        if (repository.deleteByAcademicoIdAndCertificadoId(academicoId, certificadoId) == 0) {
            throw new ResourceNotFoundException("Acadêmico " + academicoId + " não vinculado ao certificado " + certificadoId);
        }
    }

    // LinkedHashSet descarta as linhas repetidas no arquivo e mantém a ordem original.
    // Valida o arquivo inteiro antes de qualquer acesso ao banco.
    private Set<CsvRow> readCsv(MultipartFile file) throws IOException {
        Set<CsvRow> rows = new LinkedHashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 || line.isBlank()) {
                    continue;
                }
                if (!rows.add(parseLine(lineNumber, line.split("[,;]", -1)))) {
                    logger.info("Linha {} duplicada no arquivo, ignorando", lineNumber);
                }
            }
        }
        return rows;
    }

    private CsvRow parseLine(int lineNumber, String[] columns) {
        if (columns.length < 5) {
            throw new IllegalArgumentException("Linha " + lineNumber + " inválida, formato esperado: academico,email,cpf,ra,ch");
        }

        String cpf = columns[2].trim();
        if (!CpfUtils.isValid(cpf)) {
            throw new IllegalArgumentException("Linha " + lineNumber + " possui CPF inválido: " + cpf);
        }

        AcademicoRequestDto academicoDto = new AcademicoRequestDto(
                columns[0].trim(),
                columns[1].trim(),
                cpf,
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

        return new CsvRow(academicoDto, ch);
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

    // Record compara por valor, então duas linhas com os mesmos dados são iguais no Set.
    private record CsvRow(AcademicoRequestDto academico, Integer ch) {
    }
}
