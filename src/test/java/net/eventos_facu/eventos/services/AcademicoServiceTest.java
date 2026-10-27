package net.eventos_facu.eventos.services;

import net.eventos_facu.eventos.dto.academicos.AcademicoResponseDto;
import net.eventos_facu.eventos.entities.Academicos;
import net.eventos_facu.eventos.mapper.AcademicoMapper;
import net.eventos_facu.eventos.repositories.AcademicosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AcademicoService")
public class AcademicoServiceTest {

    @Mock
    private AcademicosRepository repository;
    @Mock
    private AcademicoMapper mapper;

    @InjectMocks
    private AcademicoService service;

    private static final Long ACADEMICO_ID = 1L;
    private static final String ACADEMICO = "Ana Beatriz Souza";
    private static final String EMAIL = "ana.souza@aluno.facu.edu.br";
    private static final String CPF = "529.982.247-25";
    private static final String RA = "RA2023001";
    private final Instant created = Instant.now();

    private Academicos buildEntity() {
        Academicos academico = new Academicos();
        academico.setId(ACADEMICO_ID);
        academico.setAcademico(ACADEMICO);
        academico.setEmail(EMAIL);
        academico.setCpf(CPF);
        academico.setRa(RA);
        academico.setCreated(created);
        return academico;
    }

    private AcademicoResponseDto buildResponseDto(Academicos academico) {
        return new AcademicoResponseDto(academico.getId(), academico.getAcademico(), academico.getEmail(),
                academico.getCpf(), academico.getRa(), null, null, academico.getCreated(), null);
    }

    @Nested
    @DisplayName("Search Academicos")
    class Search {

        @Test
        @DisplayName("deve buscar com o termo normalizado e ignorar o sort do cliente")
        void shouldSearchWithNormalizedTermWithoutSort() {
            // Arrange
            Academicos entity = buildEntity();
            AcademicoResponseDto response = buildResponseDto(entity);
            Pageable pageable = PageRequest.of(1, 5, Sort.by("academico"));
            Pageable semSort = PageRequest.of(1, 5);

            when(repository.search("ana beatriz", semSort))
                    .thenReturn(new PageImpl<>(List.of(entity), semSort, 6));
            when(mapper.toDtoResponse(entity)).thenReturn(response);

            // Act
            Page<AcademicoResponseDto> result = service.search("Ana Beatriz", pageable);

            // Assert
            assertThat(result.getContent()).containsExactly(response);
            assertThat(result.getTotalElements()).isEqualTo(6);

            verify(repository).search("ana beatriz", semSort);
        }

        @Test
        @DisplayName("deve quebrar CPF formatado em tokens numéricos")
        void shouldSearchCpfAsNumericTokens() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);

            when(repository.search("529 982 247 25", pageable)).thenReturn(Page.empty(pageable));

            // Act
            service.search(CPF, pageable);

            // Assert
            verify(repository).search("529 982 247 25", pageable);
        }

        @Test
        @DisplayName("deve retornar página vazia sem consultar o banco quando não há palavra pesquisável")
        void shouldReturnEmptyPageWhenNothingSearchable() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);

            // Act
            Page<AcademicoResponseDto> result = service.search("de com", pageable);

            // Assert
            assertThat(result.isEmpty()).isTrue();

            verifyNoInteractions(repository);
        }
    }
}
