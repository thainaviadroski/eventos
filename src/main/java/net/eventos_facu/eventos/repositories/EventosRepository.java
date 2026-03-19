package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventosRepository extends JpaRepository<Eventos, Long> {


    Optional<List<Eventos>> findByEvento(String evento);

    Optional<Eventos> findBySlug(String slug);
}
