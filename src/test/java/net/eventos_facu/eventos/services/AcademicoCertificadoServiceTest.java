package net.eventos_facu.eventos.services;

import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoResponseDto;
import net.eventos_facu.eventos.entities.AcademicosCertificados;
import net.eventos_facu.eventos.exception.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.AcademicoCertificadoMapper;
import net.eventos_facu.eventos.repositories.AcademicosCertificadosRepository;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AcademicoCertificadoService")
public class AcademicoCertificadoServiceTest {

    @Mock
    private AcademicosCertificadosRepository repository;
    @Mock
    private AcademicosRepository academicosRepository;
    @Mock
    private AcademicoCertificadoMapper mapper;

    @InjectMocks
    private AcademicoCertificadoService service;

    private static final Long ACADEMICO_ID = 1L;

    @Nested
    @DisplayName("Find certificados by Academico")
    class FindByAcademico {

        @Test
        @DisplayName("deve retornar página com os certificados do acadêmico")
        void shouldReturnCertificadosDoAcademico() {
            // Arrange
            AcademicosCertificados entity = new AcademicosCertificados();
            AcademicoCertificadoResponseDto response = new AcademicoCertificadoResponseDto(
                    UUID.randomUUID(), ACADEMICO_ID, "Ana Beatriz Souza", 10L, 20, Instant.now());
            Pageable pageable = PageRequest.of(0, 10);

            when(academicosRepository.existsById(ACADEMICO_ID)).thenReturn(true);
            when(repository.findByAcademicoId(ACADEMICO_ID, pageable))
                    .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
            when(mapper.toDtoResponse(entity)).thenReturn(response);

            // Act
            Page<AcademicoCertificadoResponseDto> result = service.findByAcademicoId(ACADEMICO_ID, pageable);

            // Assert
            assertThat(result.getContent()).containsExactly(response);
            assertThat(result.getTotalElements()).isEqualTo(1);

            verify(repository).findByAcademicoId(ACADEMICO_ID, pageable);
        }

        @Test
        @DisplayName("deve lançar exceção quando o acadêmico não existe")
        void shouldThrowWhenAcademicoNotFound() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);

            when(academicosRepository.existsById(ACADEMICO_ID)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> service.findByAcademicoId(ACADEMICO_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);

            verifyNoInteractions(repository);
        }
    }
}
