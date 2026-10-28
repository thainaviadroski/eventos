package net.eventos_facu.eventos.services;

import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoResponseDto;
import net.eventos_facu.eventos.entities.Academicos;
import net.eventos_facu.eventos.entities.AcademicosCertificados;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.exception.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.AcademicoCertificadoMapper;
import net.eventos_facu.eventos.mapper.AcademicoMapper;
import net.eventos_facu.eventos.repositories.AcademicosCertificadosRepository;
import net.eventos_facu.eventos.repositories.AcademicosRepository;
import net.eventos_facu.eventos.repositories.CertificadosRepository;
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
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
    private CertificadosRepository certificadosRepository;
    @Mock
    private AcademicoCertificadoMapper mapper;
    @Mock
    private AcademicoMapper academicoMapper;

    @InjectMocks
    private AcademicoCertificadoService service;

    private static final Long ACADEMICO_ID = 1L;
    private static final Long CERTIFICADO_ID = 10L;
    private static final String CPF = "529.982.247-25";
    private static final String RA = "RA001";

    @Nested
    @DisplayName("Import from file")
    class ImportFromFile {

        private MockMultipartFile csv(String content) {
            return new MockMultipartFile("file", "academicos.csv", "text/csv", content.getBytes(StandardCharsets.UTF_8));
        }

        private Academicos academico() {
            Academicos academico = new Academicos();
            academico.setId(ACADEMICO_ID);
            return academico;
        }

        @Test
        @DisplayName("deve ignorar linhas duplicadas no arquivo")
        void shouldSkipDuplicatedLines() throws Exception {
            // Arrange: a linha 3 repete a 2 com outro separador e espaços
            MockMultipartFile file = csv("""
                    academico,email,cpf,ra,ch
                    Ana Souza,ana@aluno.facu.edu.br,529.982.247-25,RA001,20
                     Ana Souza ; ana@aluno.facu.edu.br ; 529.982.247-25 ; RA001 ; 20
                    """);
            AcademicoCertificadoResponseDto response = new AcademicoCertificadoResponseDto(
                    UUID.randomUUID(), ACADEMICO_ID, "Ana Souza", CERTIFICADO_ID, 20, Instant.now());

            when(certificadosRepository.findById(CERTIFICADO_ID))
                    .thenReturn(Optional.of(Certificados.builder().id(CERTIFICADO_ID).build()));
            when(academicosRepository.findByCpfOrRa(CPF, RA)).thenReturn(Optional.of(academico()));
            when(repository.existsByAcademicoIdAndCertificadoId(ACADEMICO_ID, CERTIFICADO_ID)).thenReturn(false);
            when(repository.save(any(AcademicosCertificados.class))).thenAnswer(inv -> inv.getArgument(0));
            when(mapper.toDtoResponse(any(AcademicosCertificados.class))).thenReturn(response);

            // Act
            List<AcademicoCertificadoResponseDto> result = service.importFromFile(CERTIFICADO_ID, file);

            // Assert
            assertThat(result).containsExactly(response);

            verify(academicosRepository, times(1)).findByCpfOrRa(CPF, RA);
            verify(repository, times(1)).save(any(AcademicosCertificados.class));
        }

        @Test
        @DisplayName("deve ignorar acadêmico já vinculado ao certificado")
        void shouldSkipExistingRelation() throws Exception {
            // Arrange
            MockMultipartFile file = csv("""
                    academico,email,cpf,ra,ch
                    Ana Souza,ana@aluno.facu.edu.br,529.982.247-25,RA001,20
                    """);

            when(certificadosRepository.findById(CERTIFICADO_ID))
                    .thenReturn(Optional.of(Certificados.builder().id(CERTIFICADO_ID).build()));
            when(academicosRepository.findByCpfOrRa(CPF, RA)).thenReturn(Optional.of(academico()));
            when(repository.existsByAcademicoIdAndCertificadoId(ACADEMICO_ID, CERTIFICADO_ID)).thenReturn(true);

            // Act
            List<AcademicoCertificadoResponseDto> result = service.importFromFile(CERTIFICADO_ID, file);

            // Assert
            assertThat(result).isEmpty();

            verify(repository, never()).save(any(AcademicosCertificados.class));
        }

        @Test
        @DisplayName("deve validar o arquivo inteiro antes de salvar")
        void shouldValidateFileBeforeSaving() {
            // Arrange: linha 2 é válida, linha 3 tem CPF inválido
            MockMultipartFile file = csv("""
                    academico,email,cpf,ra,ch
                    Ana Souza,ana@aluno.facu.edu.br,529.982.247-25,RA001,20
                    Bruno Lima,bruno@aluno.facu.edu.br,123.456.789-00,RA002,20
                    """);

            when(certificadosRepository.findById(CERTIFICADO_ID))
                    .thenReturn(Optional.of(Certificados.builder().id(CERTIFICADO_ID).build()));

            // Act & Assert
            assertThatThrownBy(() -> service.importFromFile(CERTIFICADO_ID, file))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Linha 3");

            verifyNoInteractions(academicosRepository, repository);
        }
    }

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

    @Nested
    @DisplayName("Find academicos by Certificado")
    class FindByCertificado {

        @Test
        @DisplayName("deve retornar página com os acadêmicos do certificado")
        void shouldReturnAcademicosDoCertificado() {
            // Arrange
            AcademicosCertificados entity = new AcademicosCertificados();
            AcademicoCertificadoResponseDto response = new AcademicoCertificadoResponseDto(
                    UUID.randomUUID(), ACADEMICO_ID, "Ana Beatriz Souza", CERTIFICADO_ID, 20, Instant.now());
            Pageable pageable = PageRequest.of(0, 10);

            when(certificadosRepository.existsById(CERTIFICADO_ID)).thenReturn(true);
            when(repository.findByCertificadoId(CERTIFICADO_ID, pageable))
                    .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
            when(mapper.toDtoResponse(entity)).thenReturn(response);

            // Act
            Page<AcademicoCertificadoResponseDto> result = service.findByCertificadoId(CERTIFICADO_ID, pageable);

            // Assert
            assertThat(result.getContent()).containsExactly(response);
            assertThat(result.getTotalElements()).isEqualTo(1);

            verify(repository).findByCertificadoId(CERTIFICADO_ID, pageable);
        }

        @Test
        @DisplayName("deve lançar exceção quando o certificado não existe")
        void shouldThrowWhenCertificadoNotFound() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);

            when(certificadosRepository.existsById(CERTIFICADO_ID)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> service.findByCertificadoId(CERTIFICADO_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);

            verifyNoInteractions(repository);
        }
    }

    @Nested
    @DisplayName("Remove academico from Certificado")
    class RemoveAcademico {

        @Test
        @DisplayName("deve remover o vínculo do acadêmico com o certificado")
        void shouldRemoveAcademicoDoCertificado() {
            // Arrange
            when(repository.deleteByAcademicoIdAndCertificadoId(ACADEMICO_ID, CERTIFICADO_ID)).thenReturn(1L);

            // Act
            service.removeAcademico(CERTIFICADO_ID, ACADEMICO_ID);

            // Assert
            verify(repository).deleteByAcademicoIdAndCertificadoId(ACADEMICO_ID, CERTIFICADO_ID);
        }

        @Test
        @DisplayName("deve lançar exceção quando o acadêmico não está vinculado ao certificado")
        void shouldThrowWhenVinculoNotFound() {
            // Arrange
            when(repository.deleteByAcademicoIdAndCertificadoId(ACADEMICO_ID, CERTIFICADO_ID)).thenReturn(0L);

            // Act & Assert
            assertThatThrownBy(() -> service.removeAcademico(CERTIFICADO_ID, ACADEMICO_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
