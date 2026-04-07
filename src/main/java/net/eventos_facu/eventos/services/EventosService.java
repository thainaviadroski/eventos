package net.eventos_facu.eventos.services;


import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.eventos.EventoRequestDto;
import net.eventos_facu.eventos.dto.eventos.EventosResponseDto;
import net.eventos_facu.eventos.dto.eventos.EventosUpdateDto;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.exceptions.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.EventosMapper;
import net.eventos_facu.eventos.repositories.EventosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventosService {

    private Logger logger = LoggerFactory.getLogger(EventosService.class);

    private final EventosMapper eventoMapper;
    private final EventosRepository repository;


    public EventosResponseDto createNewEvento(EventoRequestDto eventoDto) {
        Eventos evento = eventoMapper.toEntity(eventoDto);
        Optional<List<Eventos>> eventos = repository.findByEvento(eventoDto.evento());

        if (eventos.isPresent() && !eventos.get().isEmpty()) {
            int size = eventos.get().size() + 1;
            evento.setSlug(eventoDto.slug() + "-" + size);
        }
        return eventoMapper.toDtoResponse(save(evento));
    }

    private Eventos save(Eventos evento) {
        return repository.save(evento);
    }

    @Transactional(readOnly = true)
    public Page<EventosResponseDto> getAllEventos(Pageable pageable) {
        return repository.findAll(pageable).map(eventoMapper::toDto);
    }

    public EventosResponseDto findById(long id) {
        return repository.findById(id)
                .map(eventoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com id: " + id));
    }

    public EventosResponseDto findBySlug(String slug) {
        return repository.findBySlug(slug)
                .map(eventoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com slug: " + slug));
    }

    @Transactional
    public EventosResponseDto update(Long id, EventosUpdateDto evento) {
        logger.info("Updating evento with id: " + id);
        Eventos e = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id " + id));
        eventoMapper.updateEntity(e, evento);
        return eventoMapper.toDtoResponse(e);
    }

}
