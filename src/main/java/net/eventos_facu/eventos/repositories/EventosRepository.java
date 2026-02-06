package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventosRepository extends JpaRepository<Eventos, Long> {
}
