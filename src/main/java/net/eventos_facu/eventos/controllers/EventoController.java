package net.eventos_facu.eventos.controllers;

import net.eventos_facu.eventos.dto.EventoDto.EventoDto;
import net.eventos_facu.eventos.entities.Eventos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/eventos")
public class EventoController {


    @PostMapping
    public ResponseEntity<Eventos> createNewEvento(@RequestBody EventoDto evento) {
        return ResponseEntity.of(Optional.of(new Eventos()));
    }

}
