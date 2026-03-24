package net.eventos_facu.eventos.services;


import net.eventos_facu.eventos.dto.eventos.EventoRequestDto;
import net.eventos_facu.eventos.dto.eventos.EventosResponseDto;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.exceptions.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.EventosMapper;
import net.eventos_facu.eventos.repositories.EventosRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("EventosService")
public class EventosServiceTest {


    @Mock
    private EventosRepository eventosRepository;
    @Mock
    private EventosMapper eventosMapper;

    @InjectMocks
    private EventosService eventosService;

    private static final Long EVENT_ID = 1L;
    private static final String EVENTO = "evento";
    private static final String DESCRICAO = "descricao";
    private static final String SLUG = "slug-evento";
    private static final String CREATED_BY = "createdBy";
    private static final String UPDATED_BY = "updatedBy";
    private final LocalDateTime dtInicio = LocalDateTime.now();
    private final LocalDateTime dtFinal = LocalDateTime.now().plusDays(1);
    private final LocalDateTime created = LocalDateTime.now();
    private final LocalDateTime updated = LocalDateTime.now();

    private EventoRequestDto buildRequest() {
        return new EventoRequestDto(EVENTO, DESCRICAO, dtInicio, dtFinal, CREATED_BY, UPDATED_BY, created, updated);
    }

    private Eventos buildEntity() {
        return Eventos.builder()
                .id(EVENT_ID)
                .evento(EVENTO)
                .descricao(DESCRICAO)
                .slug(SLUG)
                .dtInicio(dtInicio)
                .dtFinal(dtFinal)
                .createdBy(CREATED_BY)
                .updatedBy(UPDATED_BY)
                .created(created)
                .updated(updated)
                .build();
    }

    private EventosResponseDto buildResponseDto(Eventos eventos) {
        return new EventosResponseDto(
                eventos.getId(),
                eventos.getEvento(),
                eventos.getDescricao(),
                eventos.getSlug(),
                eventos.getDtInicio(),
                eventos.getDtFinal(),
                eventos.getCreatedBy(),
                eventos.getUpdatedBy(),
                eventos.getCreated(),
                eventos.getUpdated()
        );
    }

    @Nested
    @DisplayName("Create new Eventos")
    class Create {

        @Test
        @DisplayName("deve criar evento e retornar response quando dados são válidos")
        void shouldCreateEventoSuccess() {
            // Arrange
            EventoRequestDto request = buildRequest();
            Eventos entity = buildEntity();
            EventosResponseDto expected = buildResponseDto(entity);

            when(eventosMapper.toEntity(request)).thenReturn(entity);
            when(eventosRepository.save(entity)).thenReturn(entity);
            when(eventosMapper.toDtoResponse(entity)).thenReturn(expected);

            // Act — sem essa linha os stubbings acima ficam "mortos"
            EventosResponseDto result = eventosService.createNewEvento(request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(EVENT_ID);
            assertThat(result.evento()).isEqualTo(EVENTO);

            verify(eventosMapper).toEntity(request);
            verify(eventosRepository).save(entity);
            verify(eventosMapper).toDtoResponse(entity);
        }

    }


    @Nested
    @DisplayName("Find Eventos")
    class Find {
        @Test
        @DisplayName("deve retornar página com eventos mapeados")
        void getAllEventos() {
            // Arrange
            Eventos entity = buildEntity();
            EventosResponseDto response = buildResponseDto(entity);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Eventos> page = new PageImpl<>(List.of(entity), pageable, 1);

            when(eventosRepository.findAll(pageable)).thenReturn(page);
            when(eventosMapper.toDto(entity)).thenReturn(response);

            // Act
            Page<EventosResponseDto> result = eventosService.getAllEventos(pageable);

            // Assert
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).evento()).isEqualTo(EVENTO);

            verify(eventosRepository).findAll(pageable);
        }

        @Test
        @DisplayName("deve retornar evento pelo id")
        void findById() {
            // Arrange
            Eventos entity = buildEntity();
            EventosResponseDto response = buildResponseDto(entity);

            when(eventosRepository.findById(EVENT_ID)).thenReturn(Optional.of(entity));
            when(eventosMapper.toDto(entity)).thenReturn(response);

            // Act
            EventosResponseDto result = eventosService.findById(EVENT_ID);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(EVENT_ID);

            verify(eventosRepository).findById(EVENT_ID);
        }

        @Test
        @DisplayName("deve lançar exceção quando id não existe")
        void findByIdNotFound() {
            // Arrange
            when(eventosRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> eventosService.findById(EVENT_ID))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(eventosMapper, never()).toDto(any());
        }


        @Test
        @DisplayName("deve lançar exceção quando id não existe")
        void findBySlugNotFound() {
            // Arrange
            when(eventosRepository.findBySlug(SLUG)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> eventosService.findById(EVENT_ID))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(eventosMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("deve retornar evento pelo slug")
        void findBySlug() {
            // Arrange
            Eventos entity = buildEntity();
            EventosResponseDto response = buildResponseDto(entity);

            when(eventosRepository.findBySlug(SLUG)).thenReturn(Optional.of(entity));
            when(eventosMapper.toDto(entity)).thenReturn(response);

            // Act
            EventosResponseDto result = eventosService.findBySlug(SLUG);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.slug()).isEqualTo(SLUG);

            verify(eventosRepository).findBySlug(SLUG);
        }
    }


    @Test
    void update() {
    }


}
