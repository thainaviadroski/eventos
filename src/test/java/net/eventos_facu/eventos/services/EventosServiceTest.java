package net.eventos_facu.eventos.services;


import net.eventos_facu.eventos.dto.eventos.EventoRequestDto;
import net.eventos_facu.eventos.dto.eventos.EventosResponseDto;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.mapper.EventosMapper;
import net.eventos_facu.eventos.repositories.EventosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
     //       when(eventosMapper.toDto(entity)).thenReturn(expected);

            // Act — sem essa linha os stubbings acima ficam "mortos"
            Eventos result = eventosService.createNewEvento(request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EVENT_ID);
            assertThat(result.getEvento()).isEqualTo(EVENTO);

            verify(eventosMapper).toEntity(request);
            verify(eventosRepository).save(entity);
            //verify(eventosMapper).toDto(entity);
        }

    }


    @Nested
    @DisplayName("Find Eventos")
    class Find {
        @Test
        void getAllEventos() {
        }

        @Test
        void findById() {
        }


        @Test
        void findBySlug() {
        }
    }


    @Test
    void update() {
    }


}
