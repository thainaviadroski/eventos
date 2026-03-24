package net.eventos_facu.eventos.controllers;

import net.eventos_facu.eventos.dto.eventos.EventoRequestDto;
import net.eventos_facu.eventos.dto.eventos.EventosResponseDto;
import net.eventos_facu.eventos.dto.eventos.EventosUpdateDto;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.services.EventosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/eventos")
public class EventoController {

    private Logger logger = LoggerFactory.getLogger(EventoController.class);
    private final EventosService service;

    public EventoController(EventosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventosResponseDto> createNewEvento(@RequestBody EventoRequestDto evento) {
        logger.info("Created new Evento: {}", evento);
        EventosResponseDto result = service.createNewEvento(evento);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.id()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping
    public Page<EventosResponseDto> getAllEventos(Pageable pageable) {
        return ResponseEntity.ok(service.getAllEventos(pageable)).getBody();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventosResponseDto> getEventoById(@PathVariable long id) {
        logger.info("Getting Evento with id: {}", id);
        return ResponseEntity.ofNullable(service.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<EventosResponseDto> getEventoBySlug(@PathVariable String slug) {
        logger.info("Getting Evento with Slug: {}", slug);
        return ResponseEntity.ofNullable(service.findBySlug(slug));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventosResponseDto> updateEvento(@PathVariable long id, @RequestBody EventosUpdateDto evento) {
        logger.info("Updating Evento with id: {}", id);
        EventosResponseDto response = service.update(id, evento);
        return ResponseEntity.ok(response);
    }
}
