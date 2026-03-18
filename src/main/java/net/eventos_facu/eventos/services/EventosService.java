package net.eventos_facu.eventos.services;

import net.eventos_facu.eventos.dto.EventoDto.EventoDto;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.mapper.EventosMapper;
import net.eventos_facu.eventos.repositories.EventosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventosService {

    private Logger logger = LoggerFactory.getLogger(EventosService.class);

    private final EventosMapper eventoMapper;
    private final EventosRepository repository;

    public EventosService(EventosMapper eventoMapper, EventosRepository repository) {
        this.eventoMapper = eventoMapper;
        this.repository = repository;
    }

    public Eventos createNewEvento(EventoDto eventoDto) {
        Eventos evento = eventoMapper.toEntity(eventoDto);
        Optional<List<Eventos>> eventos = repository.findByEvento(eventoDto.evento());
//        Valida se existe um slug igual caso tenha ele adicona o numero
        if (eventos.isPresent() && !eventos.get().isEmpty()) {
            int size = eventos.get().size() + 1;
            evento.setSlug(eventoDto.slug() + "-" + size);
        }


        return save(evento);
    }

    private Eventos save(Eventos evento) {
        return repository.save(evento);
    }


}
