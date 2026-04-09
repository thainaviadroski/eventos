package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.Certificados;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificadosRepository extends JpaRepository<Certificados, Long> {

    Optional<List<Certificados>> findByEventoId(Long eventoId);

}
