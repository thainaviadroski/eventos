package net.eventos_facu.eventos.services;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.academicos.AcademicoRequestDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoResponseDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoUpdateDto;
import net.eventos_facu.eventos.entities.Academicos;
import net.eventos_facu.eventos.exception.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.AcademicoMapper;
import net.eventos_facu.eventos.repositories.AcademicosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AcademicoService {

    private final Logger logger = LoggerFactory.getLogger(AcademicoService.class);

    private final AcademicosRepository repository;
    private final AcademicoMapper mapper;

    @Transactional
    public AcademicoResponseDto createNewAcademico(AcademicoRequestDto academicoDto) {
        logger.info("Creating academico");
        Academicos academico = mapper.toEntity(academicoDto);
        return mapper.toDtoResponse(repository.save(academico));
    }

    @Transactional(readOnly = true)
    public Page<AcademicoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDtoResponse);
    }

    @Transactional(readOnly = true)
    public AcademicoResponseDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDtoResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Acadêmico não encontrado com id: " + id));
    }

    @Transactional
    public AcademicoResponseDto update(Long id, AcademicoUpdateDto academicoDto) {
        logger.info("Updating academico with id: " + id);
        Academicos academico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acadêmico não encontrado com id: " + id));
        mapper.updateEntity(academico, academicoDto);
        return mapper.toDtoResponse(academico);
    }

    @Transactional
    public void remove(Long id) {
        logger.info("Removing academico with id: " + id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Acadêmico não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
