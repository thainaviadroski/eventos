package net.eventos_facu.eventos.mapper;

import net.eventos_facu.eventos.dto.EventoDto;
import net.eventos_facu.eventos.entities.Eventos;
import org.springframework.stereotype.Component;

@Component
public class EventosMapper {

    public Eventos toEntity(EventoDto evento) {
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


    EventoDto toDto(Eventos evento) {
        return new EventoDto(evento.getEvento(), evento.getDescricao(), evento.getDtInicio(), evento.getDtFinal(), evento.getCreatedBy(), evento.getUpdatedBy(), evento.getCreated(), evento.getUpdated());
    }


}
