package net.eventos_facu.eventos.services;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.academicos.AcademicoRequestDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoResponseDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoUpdateDto;
import net.eventos_facu.eventos.entities.Academicos;
import net.eventos_facu.eventos.exception.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.AcademicoMapper;
import net.eventos_facu.eventos.repositories.AcademicosRepository;
import net.eventos_facu.eventos.utils.FullTextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<AcademicoResponseDto> search(String termo, Pageable pageable) {
        String query = FullTextUtils.toNaturalLanguageQuery(termo);
        if (query.isEmpty()) {
            return Page.empty(pageable);
        }
        // Mesmo motivo de EventosService.search: query nativa, então o sort do cliente é descartado
        // e o resultado vem ordenado por relevância.
        Pageable semSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return repository.search(query, semSort).map(mapper::toDtoResponse);
    }

    @Transactional(readOnly = true)
    public AcademicoResponseDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDtoResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Acadêmico não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public AcademicoResponseDto findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toDtoResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Academico não encontrado"));
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
