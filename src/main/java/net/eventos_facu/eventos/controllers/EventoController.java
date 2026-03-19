package net.eventos_facu.eventos.controllers;

import net.eventos_facu.eventos.dto.EventoDto;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.services.EventosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/eventos")
public class EventoController {

    private Logger logger = LoggerFactory.getLogger(EventoController.class);
    private final EventosService service;

    public EventoController(EventosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Eventos> createNewEvento(@RequestBody EventoDto evento) {
        logger.info("Created new Evento: {}", evento);
        return ResponseEntity.of(Optional.of(service.createNewEvento(evento)));
    }

    @GetMapping
    public Page<Eventos> getAllEventos(Pageable pageable) {
        return ResponseEntity.ok(service.getAllEventos(pageable)).getBody();
    }
}
