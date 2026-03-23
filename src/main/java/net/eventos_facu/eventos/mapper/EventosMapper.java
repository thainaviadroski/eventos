package net.eventos_facu.eventos.mapper;

import net.eventos_facu.eventos.dto.eventos.EventoRequestDto;
import net.eventos_facu.eventos.dto.eventos.EventosResponseDto;
import net.eventos_facu.eventos.dto.eventos.EventosUpdateDto;
import net.eventos_facu.eventos.entities.Eventos;
import org.springframework.stereotype.Component;

@Component
public class EventosMapper {

    public Eventos toEntity(EventoRequestDto evento) {
        return Eventos.builder()
                .evento(evento.evento())
                .descricao(evento.descricao())
                .slug(evento.slug())
                .dtInicio(evento.dtInicio())
                .dtFinal(evento.dtFinal())
                .createdBy(evento.createdBy())
                .created(evento.created())
                .updatedBy(evento.updatedBy())
                .updated(evento.updated())
                .build();
    }


    public EventosResponseDto toDto(Eventos evento) {
        return new EventosResponseDto(evento.getId(), evento.getEvento(), evento.getDescricao(), evento.getSlug(), evento.getDtInicio(),
                evento.getDtFinal(), evento.getCreatedBy(), evento.getUpdatedBy(), evento.getCreated(),
                evento.getUpdated());
    }

    public EventosResponseDto toDtoResponse(Eventos evento) {
        return new EventosResponseDto(evento.getId(), evento.getEvento(), evento.getDescricao(), evento.getSlug(), evento.getDtInicio(),
                evento.getDtFinal(), evento.getCreatedBy(), evento.getUpdatedBy(), evento.getCreated(),
                evento.getUpdated());
    }

    public void updateEntity(Eventos evento, EventosUpdateDto eventosUpdateDto) {
        evento.setEvento(eventosUpdateDto.evento());
        evento.setDescricao(eventosUpdateDto.descricao());
        evento.setDtInicio(eventosUpdateDto.dtInicio());
        evento.setDtFinal(eventosUpdateDto.dtFinal());
        evento.setSlug(eventosUpdateDto.slug());

    }
}
